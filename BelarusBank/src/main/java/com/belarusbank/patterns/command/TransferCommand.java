package com.belarusbank.patterns.command;

import com.belarusbank.dao.UserDao;
import com.belarusbank.model.Account;
import com.belarusbank.model.TransactionType;
import java.math.BigDecimal;

public class TransferCommand extends BankTransaction {
    private Account to;
    private UserDao dao;

    public TransferCommand(Account from, Account to, BigDecimal amount, UserDao dao) {
        super(TransactionType.TRANSFER, from, amount);
        this.to = to;
        this.dao = dao;
    }

    @Override
    public void execute() {
        if (account.getId().equals(to.getId())) {
             throw new IllegalArgumentException("Нельзя перевести деньги самому себе");
        }
        
        Account first = account.getId().compareTo(to.getId()) < 0 ? account : to;
        Account second = account.getId().compareTo(to.getId()) < 0 ? to : account;

        first.getLock().lock();
        try {
            second.getLock().lock();
            try {
                account.withdraw(amount);
                to.deposit(amount);
                dao.updateAccount(account);
                dao.updateAccount(to);
            } finally {
                second.getLock().unlock();
            }
        } finally {
            first.getLock().unlock();
        }
    }
    
    @Override
    public String getDescription() {
        return super.getDescription() + " -> ПОЛУЧАТЕЛЬ: " + to.getId();
    }
}
