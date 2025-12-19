package bsu.famcs.nikonchik.lab2.backend.entities.events.lifecycleevents;

import bsu.famcs.nikonchik.lab2.backend.entities.events.Event;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "account_freeze_events")
public class AccountFreezeEvent extends Event {
    private static final long serialVersionUID = 1L;

    @Column(name = "account_id", nullable = false, updatable = false)
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, updatable = false, length = 20)
    private ActionType actionType;

    protected AccountFreezeEvent() {
        super();
        this.accountId = null;
        this.actionType = null;
    }

    public AccountFreezeEvent(UUID eventId, UUID accountId, UUID initiatorId,
                              LocalDateTime timestamp, EventStatus status,
                              String description, ActionType actionType) {
        super(eventId, initiatorId, timestamp, status, description);
        this.accountId = Objects.requireNonNull(accountId,
                "Account cannot be null");
        this.actionType = Objects.requireNonNull(actionType,
                "Event type cannot be null");
    }

    public enum ActionType {
        FREEZE,
        UNFREEZE
    }

    public UUID getAccount() { return accountId; }
    public ActionType getActionType() { return actionType; }

    @Override
    public String toString() {
        return "AccountFreezeEvent{" +
                "eventId=" + eventId +
                ", accountId=" + accountId +
                ", eventType=" + actionType +
                ", performedBy=" + initiatorId +
                ", createdAt=" + timestamp +
                ", description='" + description + '\'' +
                '}';
    }
}