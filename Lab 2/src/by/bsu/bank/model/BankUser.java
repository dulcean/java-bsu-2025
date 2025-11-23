package by.bsu.bank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BankUser {
    private final UUID userId;
    private String nickname;
    private final List<BankAccount> accounts;

    public BankUser(UUID userId, String nickname, List<BankAccount> accounts) {
        this.userId = userId;
        this.nickname = nickname;
        this.accounts = new ArrayList<>(accounts);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<BankAccount> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    @Override
    public String toString() {
        return "BankUser{" +
                "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
