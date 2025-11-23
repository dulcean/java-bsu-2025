package by.bsu.bank.repository;

import by.bsu.bank.model.BankUser;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentMap<UUID, BankUser> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<BankUser> findById(UUID userId) {
        return Optional.ofNullable(storage.get(userId));
    }

    @Override
    public void save(BankUser user) {
        storage.put(user.getUserId(), user);
    }
}
