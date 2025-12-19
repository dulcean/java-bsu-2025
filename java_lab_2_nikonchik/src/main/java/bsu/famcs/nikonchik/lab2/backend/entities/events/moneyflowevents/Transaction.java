package bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Objects;

import bsu.famcs.nikonchik.lab2.backend.entities.events.Event;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING, length = 15)
public abstract class Transaction extends Event {
    private static final long serialVersionUID = 7L;

    @Column(name = "amount", nullable = false, updatable = false, precision = 15, scale = 2)
    protected final BigDecimal amount;

    protected Transaction() {
        super();
        this.amount = null;
        this.status = null;
        this.failureReason = null;
    }

    protected Transaction(UUID eventId, UUID initiatorId,
                          BigDecimal amount, LocalDateTime timestamp,
                          EventStatus status, String description) {
        super(eventId, initiatorId, timestamp, status, description);
        this.amount = Objects.requireNonNull(amount,
                "Transaction amount cannot be null");
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', amount=%s, timestamp=%s, status='%s'}",
                eventId, amount, timestamp, status);
    }

    public BigDecimal getAmount() { return amount; }
}
