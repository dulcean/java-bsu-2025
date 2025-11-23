package by.bsu.bank.repository;

import by.bsu.bank.model.BankUser;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<BankUser> findById(UUID userId);
    void save(BankUser user);
}
