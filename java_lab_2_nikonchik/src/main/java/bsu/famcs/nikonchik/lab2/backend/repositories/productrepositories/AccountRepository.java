package bsu.famcs.nikonchik.lab2.backend.repositories.productrepositories;

import bsu.famcs.nikonchik.lab2.backend.entities.products.Account;
import bsu.famcs.nikonchik.lab2.backend.entities.products.Account.AccountStatus;
import bsu.famcs.nikonchik.lab2.backend.entities.products.Account.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCurrency(Currency currency);
    List<Account> findByStatus(AccountStatus status);
}

