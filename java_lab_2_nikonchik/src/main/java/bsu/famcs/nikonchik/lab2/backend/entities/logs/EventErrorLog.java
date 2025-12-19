package bsu.famcs.nikonchik.lab2.backend.entities.logs;

import bsu.famcs.nikonchik.lab2.backend.entities.events.Event;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;

@MappedSuperclass
public abstract class EventErrorLog implements Serializable,
        Comparable<EventErrorLog> {
    private static final long serialVersionUID = 3L;

    @Id
    @Column(name = "log_id", nullable = false, unique = true, updatable = false)
    protected final UUID logId;

    @Column(name = "event_id", nullable = false, unique = true, updatable = false)
    protected final UUID eventId;

    @Column(name = "initiator_id", nullable = false, unique = true, updatable = false)
    protected final UUID initiatorId;

    @Column(name = "failed_at", nullable = false)
    protected final LocalDateTime timestamp;

    @Column(name = "error_code", nullable = false, length = 50, updatable = false)
    protected final String errorCode;

    @Column(name = "error_message", nullable = false, length = 600, updatable = false)
    protected final String errorMessage;

    @Column(name = "stacktrace", nullable = false, columnDefinition = "TEXT", updatable = false)
    protected final String stacktrace;

    protected EventErrorLog() {
        this.logId = null;
        this.eventId = null;
        this.initiatorId = null;
        this.timestamp = null;
        this.errorCode = null;
        this.errorMessage = null;
        this.stacktrace = null;
    }

    protected EventErrorLog(UUID logId, UUID eventId,
                            UUID initiatorId, LocalDateTime timestamp,
                            String errorCode, String errorMessage,
                            String stacktrace) {
        this.logId = Objects.requireNonNull(logId,
                "Log id cannot be null");
        this.eventId = Objects.requireNonNull(eventId,
                "Event id cannot be null");
        this.initiatorId = Objects.requireNonNull(initiatorId,
                "Initiator id cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp,
                "Timestamp cannot be null");
        this.errorCode = Objects.requireNonNull(errorCode,
                "Error code cannot be null");
        this.errorMessage = Objects.requireNonNull(errorMessage,
                "Error message cannot be null");
        this.stacktrace = Objects.requireNonNull(stacktrace,
                "Stack trace cannot be null");
    }

    @Override
    public int compareTo(EventErrorLog other) {
        return this.timestamp.compareTo(other.timestamp);
    }

    public UUID getLogId() { return logId; }
    public UUID getEventId() { return eventId; }
    public UUID getInitiatorId() { return initiatorId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public String getStacktrace() { return stacktrace; }
}
