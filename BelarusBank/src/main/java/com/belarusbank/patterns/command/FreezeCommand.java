package com.belarusbank.patterns.command;

import com.belarusbank.dao.UserDao;
import com.belarusbank.model.Account;
import com.belarusbank.model.TransactionType;
import java.math.BigDecimal;

public class FreezeCommand extends BankTransaction {
    private UserDao dao;

    public FreezeCommand(Account account, UserDao dao) {
        super(TransactionType.FREEZE, account, BigDecimal.ZERO);
        this.dao = dao;
    }

    @Override
    public void execute() {
        account.setFrozen(!account.isFrozen());
        dao.updateAccount(account);
    }
}
