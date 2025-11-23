package org.example.service;

import org.example.model.TransactionType;
import org.example.repo.*;

import java.util.HashMap;
import java.util.Map;

public class TransactionStrategyFactory {
    private final Map<TransactionType, TransactionStrategy> map = new HashMap<>();

    public TransactionStrategyFactory(AccountRepository accounts, TransactionRepository txRepo) {
        register(new DepositStrategy(accounts, txRepo));
        register(new WithdrawStrategy(accounts, txRepo));
        register(new FreezeStrategy(accounts, txRepo));
        register(new TransferStrategy(accounts, txRepo));
    }

    private void register(TransactionStrategy s) {
        map.put(s.type(), s);
    }

    public TransactionStrategy get(org.example.model.TransactionType type) {
        return map.get(type);
    }
}
