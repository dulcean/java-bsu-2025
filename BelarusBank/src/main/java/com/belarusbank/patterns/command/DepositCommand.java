package com.belarusbank.patterns.command;

import com.belarusbank.dao.UserDao;
import com.belarusbank.model.Account;
import com.belarusbank.model.TransactionType;
import java.math.BigDecimal;

public class DepositCommand extends BankTransaction {
    private UserDao dao;

    public DepositCommand(Account account, BigDecimal amount, UserDao dao) {
        super(TransactionType.DEPOSIT, account, amount);
        this.dao = dao;
    }

    @Override
    public void execute() {
        account.deposit(amount);
        dao.updateAccount(account);
    }
}
