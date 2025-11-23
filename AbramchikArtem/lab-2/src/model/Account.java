package model;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private UUID id;
    private BigDecimal balance;
    private boolean frozen;
    private final ReentrantLock lock = new ReentrantLock();

    public Account(UUID id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
        this.frozen = false;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze() {
        frozen = true;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", balance=" + balance + ", frozen=" + frozen + "}";
    }
}
