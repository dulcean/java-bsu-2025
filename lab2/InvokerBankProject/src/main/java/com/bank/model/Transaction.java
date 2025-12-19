package com.bank.model;

import com.bank.patterns.Visitor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private LocalDateTime timestamp;
    private ActionType action;
    private BigDecimal amount;
    private UUID accountId;
    private UUID targetAccountId;

    public Transaction(ActionType action, BigDecimal amount, UUID accountId, UUID targetAccountId) {
        this.id = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.amount = amount;
        this.accountId = accountId;
        this.targetAccountId = targetAccountId;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public ActionType getAction() {
        return action;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getTargetAccountId() {
        return targetAccountId;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
