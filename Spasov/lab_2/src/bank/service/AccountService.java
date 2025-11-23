package bank.service;

import bank.db.AccountRepository;
import bank.db.jdbc.JdbcAccountRepository;
import bank.model.Account;
import bank.model.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountService {

    private final AccountRepository accountRepo = new JdbcAccountRepository();

    public Optional<Account> find(UUID id) {
        return accountRepo.findById(id);
    }

    public void save(Account account) {
        accountRepo.save(account);
    }

    public Account create(UUID userId, BigDecimal initialBalance) {
        UUID accountId = UUID.randomUUID();
        Account account = new Account(accountId, userId, initialBalance, AccountStatus.ACTIVE);
        accountRepo.save(account);
        return account;
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }
}
