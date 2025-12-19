package bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@DiscriminatorValue("WITHDRAWAL")
public class WithdrawalTransaction extends Transaction {
    @Column(name = "from_account_id", updatable = false)
    private final UUID fromAccount;

    @Column(name = "withdrawal_location", length = 30)
    private final String withdrawalLocation;

    protected WithdrawalTransaction() {
        super();
        this.fromAccount = null;
        this.withdrawalLocation = null;
    }

    public WithdrawalTransaction(UUID id, UUID initiatorId,
                                 BigDecimal amount, LocalDateTime timestamp,
                                 EventStatus status, String description,
                                 UUID fromAccount, String withdrawalLocation) {
        super(id, initiatorId, amount, timestamp, status, description);
        this.fromAccount = Objects.requireNonNull(fromAccount, "From account cannot be null");
        this.withdrawalLocation = withdrawalLocation;
    }

    @Override
    public String toString() {
        return String.format("WithdrawalTransaction{id='%s', amount=%s, fromAccount='%s', location='%s'}",
                initiatorId, amount, fromAccount, withdrawalLocation);
    }

    public UUID getFromAccount() { return fromAccount; }
    public String getWithdrawalLocation() { return withdrawalLocation; }
}
