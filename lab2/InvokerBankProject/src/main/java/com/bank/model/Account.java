package com.bank.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private UUID id;
    private String name;
    private BigDecimal balance;
    private boolean isFrozen;

    public Account(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.balance = BigDecimal.ZERO;
        this.isFrozen = false;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    @Override
    public String toString() {
        return "Account{name='" + name + "', balance=" + balance + ", frozen=" + isFrozen + "}";
    }
}
