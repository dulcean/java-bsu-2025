package org.example.model;

import java.util.*;

public class User {
    private final UUID id;
    private String nickname;
    private final List<UUID> accountIds = new ArrayList<>();

    public User(UUID id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public UUID getId() { return id; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public List<UUID> getAccountIds() { return Collections.unmodifiableList(accountIds); }
    public void addAccount(UUID accountId) { accountIds.add(accountId); }
    public void removeAccount(UUID accountId) { accountIds.remove(accountId); }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", nickname='" + nickname + " " + ", accounts=" + accountIds + '}';
    }
}
