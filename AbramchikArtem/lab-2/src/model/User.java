package model;

import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String nickname;
    private List<Account> accounts;

    public User(UUID id, String nickname, List<Account> accounts) {
        this.id = id;
        this.nickname = nickname;
        this.accounts = accounts;
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

    @Override
    public String toString() {
        return "User{id=" + id + ", nickname='" + nickname + "', accounts=" + accounts + "}";
    }
}
