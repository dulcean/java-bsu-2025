package bank.db;

import bank.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findById(UUID id);

    void save(Account account);

    List<Account> findAll();
}
