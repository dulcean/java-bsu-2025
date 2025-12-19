package com.bank.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String nickname;
    private List<Account> accounts;

    public User(String nickname) {
        this.id = UUID.randomUUID();
        this.nickname = nickname;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getNickname() {
        return nickname;
    }

    public UUID getId() {
        return id;
    }
}
