package com.belarusbank.model;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private UUID id;
    private UUID userId;
    private BigDecimal balance;
    private boolean isFrozen;
    private final ReentrantLock lock = new ReentrantLock();

    public Account(UUID id, UUID userId, BigDecimal balance, boolean isFrozen) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.isFrozen = isFrozen;
    }

    public Account(UUID userId) {
        this(UUID.randomUUID(), userId, BigDecimal.ZERO, false);
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public void deposit(BigDecimal amount) {
        lock.lock();
        try {
            if (isFrozen) throw new IllegalStateException("Счет заморожен");
            this.balance = this.balance.add(amount);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(BigDecimal amount) {
        lock.lock();
        try {
            if (isFrozen) throw new IllegalStateException("Счет заморожен");
            if (this.balance.compareTo(amount) < 0) throw new IllegalStateException("Недостаточно средств");
            this.balance = this.balance.subtract(amount);
        } finally {
            lock.unlock();
        }
    }

    public ReentrantLock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return id.toString().substring(0, 8) + "... | " + balance + " BYN " + (isFrozen ? "[ЗАМОРОЖЕН]" : "");
    }
}
