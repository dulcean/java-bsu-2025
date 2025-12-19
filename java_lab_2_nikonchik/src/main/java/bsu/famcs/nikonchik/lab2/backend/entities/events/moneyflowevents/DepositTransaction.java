package bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@DiscriminatorValue("DEPOSIT")
public class DepositTransaction extends Transaction {
    @Column(name = "to_account_id", updatable = false, nullable = false, unique = true)
    private final UUID toAccount;

    @Column(name = "deposit_method", updatable = false, nullable = false, length = 20)
    private final String depositMethod;

    protected DepositTransaction() {
        super();
        this.toAccount = null;
        this.depositMethod = null;
    }

    public DepositTransaction(UUID id, UUID initiatorId,
                              BigDecimal amount, LocalDateTime timestamp,
                              EventStatus status, String description,
                              UUID toAccount, String depositMethod) {
        super(id, initiatorId, amount, timestamp, status, description);
        this.toAccount = Objects.requireNonNull(toAccount,
                "To account cannot be null");
        this.depositMethod = Objects.requireNonNull(depositMethod,
                "Deposit method cannot be null");
    }

    @Override
    public String toString() {
        return String.format("DepositTransaction{id='%s', amount=%s, toAccount='%s', method='%s'}",
                eventId, amount, toAccount, depositMethod);
    }

    public UUID getToAccount() { return toAccount; }
    public String getDepositMethod() { return depositMethod; }
}
