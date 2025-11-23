package com.bank.model;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final UUID id;
    private BigDecimal balance;
    private boolean isFrozen;
    private final ReentrantLock lock;

    public Account(BigDecimal initialBalance) {
        this.id = UUID.randomUUID();
        this.balance = initialBalance;
        this.isFrozen = false;
        this.lock = new ReentrantLock();
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        try {
            lock.lock();
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public boolean isFrozen() {
        try {
            lock.lock();
            return isFrozen;
        } finally {
            lock.unlock();
        }
    }

    public void setFrozen(boolean frozen) {
        try {
            lock.lock();
            this.isFrozen = frozen;
        } finally {
            lock.unlock();
        }
    }

    public void deposit(BigDecimal amount) {
        try {
            lock.lock();
            if (isFrozen) throw new IllegalStateException("Account is frozen");
            this.balance = this.balance.add(amount);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(BigDecimal amount) {
        try {
            lock.lock();
            if (isFrozen) throw new IllegalStateException("Account is frozen");
            if (this.balance.compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient funds");
            this.balance = this.balance.subtract(amount);
        } finally {
            lock.unlock();
        }
    }

    public Lock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return "Account{id=" + id + ", balance=" + balance + ", isFrozen=" + isFrozen + '}';
    }
}