package bank.model;

import bank.model.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private UUID id;
    private UUID userId;
    private BigDecimal balance;
    private AccountStatus status;

    public Account(UUID id, UUID userId, BigDecimal balance, AccountStatus status) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}
