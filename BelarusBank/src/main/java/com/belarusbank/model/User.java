package com.belarusbank.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String nickname;
    private List<Account> accounts;

    public User(UUID id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        this.accounts = new ArrayList<>();
    }

    public User(String nickname) {
        this(UUID.randomUUID(), nickname);
    }

    public UUID getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    @Override
    public String toString() {
        return nickname;
    }
}
