package org.example.service;

import org.example.model.*;
import org.example.repo.*;

import java.util.Optional;

public class TransferStrategy implements TransactionStrategy {
    private final AccountRepository accounts;
    private final TransactionRepository txRepo;

    public TransferStrategy(AccountRepository accounts, TransactionRepository txRepo) {
        this.accounts = accounts;
        this.txRepo = txRepo;
    }

    @Override
    public boolean execute(TransactionRecord tx) {
        if (tx.getTargetAccountId() == null) return false;
        Optional<Account> fromOpt = accounts.findById(tx.getAccountId());
        Optional<Account> toOpt = accounts.findById(tx.getTargetAccountId());
        if (fromOpt.isEmpty() || toOpt.isEmpty()) return false;
        Account from = fromOpt.get();
        Account to = toOpt.get();

        synchronized (from) {
            synchronized (to) {
                if (!from.withdraw(tx.getAmount())) return false;
                to.deposit(tx.getAmount());
            }
        }
        accounts.save(from);
        accounts.save(to);
        txRepo.save(tx);
        return true;
    }

    @Override
    public org.example.model.TransactionType type() { return org.example.model.TransactionType.TRANSFER; }
}
