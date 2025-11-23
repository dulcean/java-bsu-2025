package com.belarusbank.patterns.command;

import com.belarusbank.model.Account;
import com.belarusbank.model.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public abstract class BankTransaction {
    protected UUID id;
    protected long timestamp;
    protected TransactionType action;
    protected Account account;
    protected BigDecimal amount;

    public BankTransaction(TransactionType action, Account account, BigDecimal amount) {
        this.id = UUID.randomUUID();
        this.timestamp = Instant.now().toEpochMilli();
        this.action = action;
        this.account = account;
        this.amount = amount;
    }

    public abstract void execute() throws Exception;
    
    public String getDescription() {
        return String.format("[%s] %s %s BYN на счете %s", timestamp, action, amount, account.getId());
    }
}
