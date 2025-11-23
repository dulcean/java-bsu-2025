package by.bsu.bank.repository;

import by.bsu.bank.model.BankAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<BankAccount> findById(UUID accountId);
    List<BankAccount> findAllByUserId(UUID userId);
    void save(BankAccount account, UUID userId);
}
