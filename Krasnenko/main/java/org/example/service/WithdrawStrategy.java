package org.example.service;

import org.example.model.*;
import org.example.repo.*;

import java.util.Optional;

public class WithdrawStrategy implements TransactionStrategy {
    private final AccountRepository accounts;
    private final TransactionRepository txRepo;

    public WithdrawStrategy(AccountRepository accounts, TransactionRepository txRepo) {
        this.accounts = accounts;
        this.txRepo = txRepo;
    }

    @Override
    public boolean execute(TransactionRecord tx) {
        Optional<Account> opt = accounts.findById(tx.getAccountId());
        if (opt.isEmpty()) return false;
        Account acc = opt.get();
        boolean ok = acc.withdraw(tx.getAmount());
        if (ok) {
            accounts.save(acc);
            txRepo.save(tx);
        }
        return ok;
    }

    @Override
    public TransactionType type() { return TransactionType.WITHDRAW; }
}
