package bank.model;

import bank.model.enums.TransactionStatus;
import bank.model.enums.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final TransactionType type;
    private UUID id;
    private UUID accountId;
    private UUID targetAccountId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private TransactionStatus status;
    private String note;
    private BigDecimal accountBalanceAfter;
    private BigDecimal targetAccountBalanceAfter;

    public Transaction(UUID id, UUID accountId, UUID targetAccountId, BigDecimal amount, LocalDateTime timestamp, TransactionType type) {
        this.id = id;
        this.accountId = accountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
        this.status = TransactionStatus.PENDING;
        this.note = null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(UUID targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getAccountBalanceAfter() {
        return accountBalanceAfter;
    }

    public void setAccountBalanceAfter(BigDecimal v) {
        accountBalanceAfter = v;
    }

    public BigDecimal getTargetAccountBalanceAfter() {
        return targetAccountBalanceAfter;
    }

    public void setTargetAccountBalanceAfter(BigDecimal v) {
        targetAccountBalanceAfter = v;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", targetAccountId=" + targetAccountId +
                ", amount=" + amount +
                ", timestamp=" + Timestamp.valueOf(timestamp) +
                ", type=" + type +
                ", status=" + status +
                ", note='" + note + '\'' +
                '}';
    }
}
