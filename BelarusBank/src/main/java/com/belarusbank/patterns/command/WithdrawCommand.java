package com.belarusbank.patterns.command;

import com.belarusbank.dao.UserDao;
import com.belarusbank.model.Account;
import com.belarusbank.model.TransactionType;
import java.math.BigDecimal;

public class WithdrawCommand extends BankTransaction {
    private UserDao dao;

    public WithdrawCommand(Account account, BigDecimal amount, UserDao dao) {
        super(TransactionType.WITHDRAW, account, amount);
        this.dao = dao;
    }

    @Override
    public void execute() {
        account.withdraw(amount);
        dao.updateAccount(account);
    }
}
