package bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@DiscriminatorValue("TRANSFER")
public class TransferTransaction extends Transaction {
    @Column(name = "from_account_id", nullable = false, updatable = false)
    private final UUID fromAccount;

    @Column(name = "to_account_id", nullable = false, updatable = false)
    private final UUID toAccount;

    protected TransferTransaction() {
        super();
        this.fromAccount = null;
        this.toAccount = null;
    }

    public TransferTransaction(UUID eventId, UUID initiatorId,
                               BigDecimal amount, LocalDateTime timestamp,
                               EventStatus status, String description,
                               UUID fromAccount, UUID toAccount) {
        super(eventId, initiatorId, amount, timestamp, status, description);
        this.fromAccount = Objects.requireNonNull(fromAccount, "From account cannot be null");
        this.toAccount = Objects.requireNonNull(toAccount, "To account cannot be null");
    }

    @Override
    public String toString() {
        return String.format("TransferTransaction{id='%s', amount=%s, fromAccount='%s', toAccount='%s'}",
                eventId, amount, fromAccount, toAccount);
    }

    public UUID getFromAccount() { return fromAccount; }
    public UUID getToAccount() { return toAccount; }
}
