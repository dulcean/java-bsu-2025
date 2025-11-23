package org.example.service;

import org.example.model.*;
import org.example.repo.*;

import java.util.Optional;

public class DepositStrategy implements TransactionStrategy {
    private final AccountRepository accounts;
    private final TransactionRepository txRepo;

    public DepositStrategy(AccountRepository accounts, TransactionRepository txRepo) {
        this.accounts = accounts;
        this.txRepo = txRepo;
    }

    @Override
    public boolean execute(TransactionRecord tx) {
        Optional<Account> opt = accounts.findById(tx.getAccountId());
        if (opt.isEmpty()) return false;
        Account acc = opt.get();
        acc.deposit(tx.getAmount());
        accounts.save(acc);
        txRepo.save(tx);
        return true;
    }

    @Override
    public TransactionType type() { return TransactionType.DEPOSIT; }
}
