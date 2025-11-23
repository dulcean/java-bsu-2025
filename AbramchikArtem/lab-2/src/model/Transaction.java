package model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Transaction {
    public enum Action { DEPOSIT, WITHDRAW, FREEZE, TRANSFER }
    public enum Status { PENDING, SUCCESS, FAILED }

    private UUID id;
    private Instant timestamp;
    private Action action;
    private BigDecimal amount;
    private Account account;
    private Account targetAccount;
    private Status status;

    public Transaction(Action action, BigDecimal amount, Account acc, Account target) {
        this.id = UUID.randomUUID();
        this.timestamp = Instant.now();
        this.action = action;
        this.amount = amount;
        this.account = acc;
        this.targetAccount = target;
        this.status = Status.PENDING;
    }

    public Action getAction() {
        return action;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Account getAccount() {
        return account;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    public void setStatus(Status st) {
        this.status = st;
    }

    @Override
    public String toString() {
        return action + " " + amount + " FROM " + account + " TO " + targetAccount;
    }
}
