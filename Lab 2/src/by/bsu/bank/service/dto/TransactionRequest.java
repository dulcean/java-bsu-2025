package by.bsu.bank.service.dto;

import by.bsu.bank.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionRequest {
    private final TransactionType type;
    private final UUID userId;
    private final UUID sourceAccountId;
    private final UUID targetAccountId;
    private final BigDecimal amount;

    public TransactionRequest(TransactionType type,
                              UUID userId,
                              UUID sourceAccountId,
                              UUID targetAccountId,
                              BigDecimal amount) {
        this.type = type;
        this.userId = userId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
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
}
