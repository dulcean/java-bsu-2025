package bsu.famcs.nikonchik.lab2.backend.entities.products;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;
import java.math.BigDecimal;

import bsu.famcs.nikonchik.lab2.backend.exceptions.AccountExceptions.*;

@Entity
@Table(name = "accounts")
public class Account extends Product implements Comparable<Account> {
    private static final long serialVersionUID = 2L;

    @Column(name = "account_number", nullable = false, updatable = false, unique = true, length = 20)
    private final String accountNumber;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, updatable = false, length = 3)
    private final Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatus status;

    @Column(name = "account_name", length = 100)
    private String accountName;

    @Version
    private Long version;

    protected Account() {
        super();
        this.accountNumber = null;
        this.balance = null;
        this.currency = null;
        this.status = null;
        this.accountName = null;
    }

    public Account(UUID productId, UUID ownerId, LocalDateTime createdDate,
                   String accountNumber, String account_name, BigDecimal initialBalance,
                   Currency currency) {
        super(productId, ownerId, createdDate);
        this.accountNumber = Objects.requireNonNull(accountNumber, "Account number cannot be null");
        this.balance = Objects.requireNonNull(initialBalance, "Initial balance cannot be null");
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
        this.status = AccountStatus.ACTIVE;
        this.accountName = Objects.requireNonNull(accountName);
    }

    public enum Currency { USD, EUR, GBP, JPY, BYN }
    public enum AccountStatus { ACTIVE, FROZEN }

    public void withdraw(BigDecimal amount) {
        validateAccountActive();
        validateSufficientFunds(amount);
        this.balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        validateAccountActive();
        this.balance = balance.add(amount);
    }

    private void validateAccountActive() {
        if (status != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(
                    String.format("Account %s is not active. Current status: %s",
                            accountNumber, status)
            );
        }
    }

    private void validateSufficientFunds(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds in account %s. Balance: %s %s, Requested: %s %s",
                            accountNumber, balance, currency, amount, currency)
            );
        }
    }

    @Override
    public int compareTo(Account other) {
        return this.balance.compareTo(other.balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accountNumber);
    }

    @Override
    public String toString() {
        return String.format("Account{id='%s', number='%s', balance=%s %s, status=%s, name='%s'}",
                getProductId(), accountNumber, balance, currency, status,
                accountName != null ? accountName : "N/A");
    }

    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public Currency getCurrency() { return currency; }
    public AccountStatus getStatus() { return status; }
    public boolean isActive() { return status == AccountStatus.ACTIVE; }
    public boolean isFrozen() { return status == AccountStatus.FROZEN; }
    public String getAccountName() { return accountName; }

    public void setBalance(BigDecimal balance) {
        this.balance = Objects.requireNonNull(balance, "Balance cannot be null");
    }

    public void activate() {
        this.status = AccountStatus.ACTIVE;
    }

    public void freeze() {
        this.status = AccountStatus.FROZEN;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
