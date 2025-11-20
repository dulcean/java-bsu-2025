package com.banking.system.model;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final UUID uuid;
    private final UUID clientUuid;
    private volatile double money;
    private volatile boolean blocked;

    private final transient ReentrantLock balanceLock = new ReentrantLock();

    public BankAccount(UUID uuid, UUID clientUuid, double money, boolean blocked) {
        this.uuid = uuid;
        this.clientUuid = clientUuid;
        this.money = money;
        this.blocked = blocked;
    }

    public BankAccount(UUID clientUuid) {
        this(UUID.randomUUID(), clientUuid, 0.0, false);
    }

    public UUID getUuid() { return uuid; }
    public UUID getClientUuid() { return clientUuid; }
    public double getMoney() { return money; }
    public void setMoney(double money) { this.money = money; }
    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    public void secureAccess() { balanceLock.lock(); }
    public void releaseAccess() { balanceLock.unlock(); }
}