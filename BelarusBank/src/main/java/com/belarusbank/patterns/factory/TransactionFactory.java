package com.belarusbank.patterns.factory;

import com.belarusbank.dao.UserDao;
import com.belarusbank.model.Account;
import com.belarusbank.model.TransactionType;
import com.belarusbank.patterns.command.*;
import java.math.BigDecimal;

public class TransactionFactory {
    private UserDao dao;

    public TransactionFactory(UserDao dao) {
        this.dao = dao;
    }

    public BankTransaction createTransaction(TransactionType type, Account source, Account target, BigDecimal amount) {
        switch (type) {
            case DEPOSIT:
                return new DepositCommand(source, amount, dao);
            case WITHDRAW:
                return new WithdrawCommand(source, amount, dao);
            case TRANSFER:
                return new TransferCommand(source, target, amount, dao);
            case FREEZE:
                return new FreezeCommand(source, dao);
            default:
                throw new IllegalArgumentException("Неизвестный тип транзакции");
        }
    }
}
