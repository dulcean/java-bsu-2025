package com.bank.transaction;

import com.bank.model.TransactionStatus;
import java.time.Instant;
import java.util.UUID;

public interface TransactionCommand {
    void execute();
    UUID getTransactionId();
    TransactionStatus getStatus();
    Instant getTimestamp();
}