package com.bank.repository;

import com.bank.model.Account;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BankRepository {
    private static final BankRepository INSTANCE = new BankRepository();
    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    private BankRepository() {}

    public static BankRepository getInstance() {
        return INSTANCE;
    }

    public void saveAccount(Account account) {
        accounts.put(account.getId(), account);
    }

    public Account getAccount(UUID id) {
        if (!accounts.containsKey(id)) throw new IllegalArgumentException("Account not found: " + id);
        return accounts.get(id);
    }
}