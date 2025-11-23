package com.bank.transaction;

import com.bank.model.TransactionStatus;
import com.bank.visitor.Visitable;
import java.time.Instant;
import java.util.UUID;

public abstract class BaseTransaction implements TransactionCommand, Visitable {
    protected final UUID id;
    protected final Instant timestamp;
    protected TransactionStatus status;

    protected BaseTransaction() {
        this.id = UUID.randomUUID();
        this.timestamp = Instant.now();
        this.status = TransactionStatus.PENDING;
    }

    @Override
    public UUID getTransactionId() {
        return id;
    }

    @Override
    public TransactionStatus getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}