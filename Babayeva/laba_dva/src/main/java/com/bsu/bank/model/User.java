package com.bsu.bank.model;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private final UUID userId; 
    private String nickname;
    private final ConcurrentHashMap<UUID, Account> accounts;

    public User(String nickname) {
        this.userId = UUID.randomUUID();
        this.nickname = nickname;
        this.accounts = new ConcurrentHashMap<>();
    }

    public UUID getUserId() { return userId; }
    public String getNickname() { return nickname; }
    public ConcurrentHashMap<UUID, Account> getAccounts() { return accounts; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public void addAccount(Account account) {
        this.accounts.put(account.getAccountId(), account);
    }

    @Override
    public String toString() {
        return String.format("User{id=%s, nickname='%s', accountsCount=%d}", 
                             userId.toString().substring(0, 8), nickname, accounts.size());
    }
}