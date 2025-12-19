package bsu.famcs.nikonchik.lab2.backend.entities.logs.lifecycleerrorlogs;

import javax.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Objects;

import bsu.famcs.nikonchik.lab2.backend.entities.logs.EventErrorLog;

@Entity
@Table(name = "freeze_errors")
public class FreezeErrorLog extends EventErrorLog {
    @Column(name = "action_type", nullable = false, length = 30, updatable = false)
    private final String actionType;

    public FreezeErrorLog() {
        super();
        this.actionType = null;
    }

    public FreezeErrorLog(UUID logId, UUID eventId,
                          UUID initiatorId, LocalDateTime timestamp,
                          String errorCode, String errorMessage,
                          String stacktrace, String actionType) {
        super(logId, eventId, initiatorId, timestamp,
                errorCode, errorMessage, stacktrace);
        this.actionType = Objects.requireNonNull(actionType,
                "Action type cannot be null");
    }

    public String getActionType() { return actionType; }
}
