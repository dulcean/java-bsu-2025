package org.example.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private final UUID id;
    private final UUID userId;
    private BigDecimal balance;
    private final String currency;
    private AccountStatus status = AccountStatus.ACTIVE;
    private long version = 0L;

    public Account(UUID id, UUID userId) {
        this(id, userId, BigDecimal.ZERO, "USD");
    }

    public Account(UUID id, UUID userId, BigDecimal balance, String currency) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.currency = currency;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public synchronized BigDecimal getBalance() { return balance; }
    public synchronized long getVersion() { return version; }

    public synchronized void deposit(java.math.BigDecimal amount) {
        if (status == AccountStatus.FROZEN) return ;
        balance = balance.add(amount);
        version++;
    }

    public synchronized boolean withdraw(java.math.BigDecimal amount) {
        if (status == AccountStatus.FROZEN) return false;
        if (balance.compareTo(amount) < 0) return false;
        balance = balance.subtract(amount);
        version++;
        return true;
    }

    public synchronized void freeze() {
        status = AccountStatus.FROZEN;
        version++;
    }

    public void toggleFreeze() {
        if (status == AccountStatus.FROZEN) {
            status = AccountStatus.ACTIVE;
        } else {
            status = AccountStatus.FROZEN;
        }
    }

    public AccountStatus getStatus() { return status; }
    public String getCurrency() { return currency; }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", userId=" + userId + ", balance=" + balance + ", status=" + status + ", version=" + version + '}';
    }
}
