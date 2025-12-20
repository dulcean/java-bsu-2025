package com.bsu.bank.model;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Модель счета пользователя.
 * Имеет собственный UUID и блокировку (Lock) для обеспечения атомарности операций.
 */
public class Account {
    private final UUID accountId;
    private double balance;
    private boolean isFrozen;
    // Lock, привязанный к конкретному счету, для обеспечения атомарности транзакций.
    private final ReentrantLock lock = new ReentrantLock();

    public Account(double initialBalance) {
        this.accountId = UUID.randomUUID();
        this.balance = initialBalance;
        this.isFrozen = false;
    }
    
    public Account(UUID accountId, double balance, boolean isFrozen) {
        this.accountId = accountId;
        this.balance = balance;
        this.isFrozen = isFrozen;
    }

    public UUID getAccountId() { return accountId; }
    public double getBalance() { return balance; }
    public boolean isFrozen() { return isFrozen; }
    public ReentrantLock getLock() { return lock; }
    public UUID getId() {
        return accountId;
    }

    // Методы, изменяющие состояние счета, вызываются только внутри защищенного Lock блока.
    public void setBalance(double balance) { this.balance = balance; }
    public void setFrozen(boolean frozen) { this.isFrozen = frozen; }

    @Override
    public String toString() {
        return String.format("Account{id=%s, balance=%.2f, isFrozen=%s}", 
                             accountId.toString().substring(0, 8), balance, isFrozen);
    }
}