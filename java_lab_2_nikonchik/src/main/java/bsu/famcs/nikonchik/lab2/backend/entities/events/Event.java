package bsu.famcs.nikonchik.lab2.backend.entities.events;

import bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents.Transaction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class Event implements Serializable, Comparable<Event> {
    private static final long serialVersionUID = 42L;

    @Id
    @Column(name = "event_id", nullable = false, updatable = false, unique = true)
    protected final UUID eventId;

    @Column(name = "initiator_id", nullable = false, updatable = false)
    protected final UUID initiatorId;

    @Column(name = "timestamp", nullable = false, updatable = false)
    protected final LocalDateTime timestamp;

    @Column(name = "description", nullable = false, length = 30)
    protected final String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    protected EventStatus status;

    @Transient
    protected Exception failureReason;

    public enum EventStatus {
        PENDING, COMPLETED, UNDONE, FAILED
    }

    protected Event() {
        this.eventId = null;
        this.initiatorId = null;
        this.timestamp = null;
        this.description = null;
        this.status = null;
        this.failureReason = null;
    }

    protected Event(UUID eventId, UUID initiatorId,
                    LocalDateTime timestamp, EventStatus status,
                    String description) {
        this.eventId = Objects.requireNonNull(eventId,
                "Event id cannot be null");
        this.initiatorId = Objects.requireNonNull(initiatorId,
                "Initiator id cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp,
                "Timestamp cannot be null");
        this.status = Objects.requireNonNull(status,
                "Event status cannot be null");
        this.description = Objects.requireNonNull(description,
                "Description cannot be null");
        this.failureReason = null;
    }

    @Override
    public int compareTo(Event other) {
        return this.timestamp.compareTo(other.timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event that = (Event) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    public void setStatus(EventStatus status) {
        this.status = Objects.requireNonNull(status);
    }
    public void setFailed(Exception failureReason) {
        this.status = EventStatus.FAILED;
        this.failureReason = failureReason;
    }
    public void setCompleted() { this.status = EventStatus.COMPLETED; }

    public UUID getEventId() { return eventId; }
    public UUID getInitiatorId() { return initiatorId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public EventStatus getStatus() { return status; }
}
