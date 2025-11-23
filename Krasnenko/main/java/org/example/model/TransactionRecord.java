package org.example.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TransactionRecord {
    private final UUID id;
    private final Instant createdAt;
    private final TransactionType type;
    private final BigDecimal amount;
    private final UUID accountId;
    private final UUID targetAccountId;
    private final UUID userId;

    public TransactionRecord(UUID id, Instant createdAt, TransactionType type, BigDecimal amount, UUID accountId, UUID targetAccountId, UUID userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.targetAccountId = targetAccountId;
        this.userId = userId;
    }

    public UUID getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public UUID getAccountId() { return accountId; }
    public UUID getTargetAccountId() { return targetAccountId; }
    public UUID getUserId() { return userId; }

    @Override
    public String toString() {
        return "TransactionRecord{" + "id=" + id + ", createdAt=" + createdAt + ", type=" + type + ", amount=" + amount + ", accountId=" + accountId + ", targetAccountId=" + targetAccountId + ", userId=" + userId + '}';
    }
}
