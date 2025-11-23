package by.bsu.bank.repository;

import by.bsu.bank.model.BankAccount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryAccountRepository implements AccountRepository {
    private final ConcurrentMap<UUID, BankAccount> accounts = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, Set<UUID>> accountsByUser = new ConcurrentHashMap<>();

    @Override
    public Optional<BankAccount> findById(UUID accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    @Override
    public List<BankAccount> findAllByUserId(UUID userId) {
        Set<UUID> accountIds = accountsByUser.getOrDefault(userId, Collections.emptySet());
        List<BankAccount> result = new ArrayList<>();
        for (UUID accountId : accountIds) {
            BankAccount account = accounts.get(accountId);
            if (account != null) {
                result.add(account);
            }
        }
        return result;
    }

    @Override
    public void save(BankAccount account, UUID userId) {
        accounts.put(account.getAccountId(), account);
        accountsByUser
                .computeIfAbsent(userId, userKey -> ConcurrentHashMap.newKeySet())
                .add(account.getAccountId());
    }
}
