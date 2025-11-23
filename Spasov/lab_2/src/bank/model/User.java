package bank.model;

import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String nickname;
    private List<UUID> accountIds;

    public User(UUID id, String nickname, List<UUID> accountIds) {
        this.id = id;
        this.nickname = nickname;
        this.accountIds = accountIds;
    }

    public void addAccount(UUID accountId) {
        accountIds.add(accountId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<UUID> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(List<UUID> accountIds) {
        this.accountIds = accountIds;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", accountIds=" + accountIds +
                '}';
    }
}
