package com.bank.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {
    private final UUID id;
    private String nickname;
    private final List<Account> accounts;

    public User(String nickname) {
        this.id = UUID.randomUUID();
        this.nickname = nickname;
        this.accounts = new CopyOnWriteArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", nickname='" + nickname + "', accounts=" + accounts + '}';
    }
}