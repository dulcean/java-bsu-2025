package com.bsu.bank.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final UUID transactionId; 
    private final LocalDateTime timestamp;
    private final TransactionAction action;
    private final double amount;
    private final UUID accountId;
    private final UUID userId;

    public Transaction(TransactionAction action, double amount, UUID accountId, UUID userId) {
        this.transactionId = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.amount = amount;
        this.accountId = accountId;
        this.userId = userId;
    }

    public UUID getTransactionId() { return transactionId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionAction getAction() { return action; }
    public double getAmount() { return amount; }
    public UUID getAccountId() { return accountId; }
    public UUID getUserId() { return userId; }

    @Override
    public String toString() {
        return String.format("Transaction{id=%s, action=%s, amount=%.2f, accountId=%s}",
                             transactionId.toString().substring(0, 8), action, amount, accountId.toString().substring(0, 8));
    }
}