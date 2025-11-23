package org.example.repo;

import org.example.model.Account;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.UUID;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<UUID, Account> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Account account) {
        store.put(account.getId(), account);
    }

    public void add(Account account) {
        save(account);
    }

    public Collection<Account> getAll() {
        return store.values();
    }
}
