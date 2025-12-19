package bsu.famcs.nikonchik.lab2.backend.entities.logs.moneyflowerrorlogs;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Objects;

import bsu.famcs.nikonchik.lab2.backend.entities.logs.EventErrorLog;

@Entity
@Table(name = "moneyflow_errors")
public class MoneyFlowErrorLog extends EventErrorLog {
    @Column(name = "amount", precision = 15, scale = 2, nullable = false, updatable = false)
    private final BigDecimal amount;

    @Column(name = "transaction_type", nullable = false, length = 30, updatable = false)
    private final String transactionType;

    public MoneyFlowErrorLog() {
        super();
        this.amount = null;
        this.transactionType = null;
    }

    public MoneyFlowErrorLog(UUID logId, UUID eventId,
                             UUID initiatorId, LocalDateTime timestamp,
                             String errorCode, String errorMessage,
                             String stacktrace, BigDecimal amount,
                             String transactionType) {
        super(logId, eventId, initiatorId, timestamp,
                errorCode, errorMessage, stacktrace);
        this.amount = Objects.requireNonNull(amount,
                "Amount cannot be null");
        this.transactionType = Objects.requireNonNull(transactionType,
                "Transaction type cannot be null");
    }

    public BigDecimal getAmount() { return amount; }
    public String getTransactionType() { return transactionType; }
 }
