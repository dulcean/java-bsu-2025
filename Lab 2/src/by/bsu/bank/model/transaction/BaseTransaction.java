package by.bsu.bank.model.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public abstract class BaseTransaction {
    private final UUID transactionId;
    private final Instant timestamp;
    private final TransactionType transactionType;
    private final UUID userId;
    private final UUID sourceAccountId;
    private final UUID targetAccountId;
    private final BigDecimal amount;

    protected BaseTransaction(UUID transactionId,
                              Instant timestamp,
                              TransactionType transactionType,
                              UUID userId,
                              UUID sourceAccountId,
                              UUID targetAccountId,
                              BigDecimal amount) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.transactionType = transactionType;
        this.userId = userId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getSourceAccountId() {
        return sourceAccountId;
    }

    public UUID getTargetAccountId() {
        return targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public abstract void accept(TransactionVisitor visitor);
}
