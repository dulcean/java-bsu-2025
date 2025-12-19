package bsu.famcs.nikonchik.lab2.backend.entities.actors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class Actor implements Serializable {
    private static final long serialVersionUID = 9L;

    @Id
    @Column(name = "actor_id", nullable = false, updatable = false, unique = true)
    protected final UUID actorId;

    @Column(name = "username", nullable = false, updatable = false, length = 50)
    protected String username;

    @Column(name = "created_date", nullable = false, updatable = false)
    protected final LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_level", nullable = false, updatable = false)
    protected final PermissionLevel permissionLevel;

    protected Actor() {
        this.actorId = null;
        this.username = null;
        this.createdDate = null;
        this.permissionLevel = null;
    }

    public Actor(UUID actorId, String username,
                 LocalDateTime createdDate,
                 PermissionLevel permissionLevel) {
        this.actorId = Objects.requireNonNull(actorId, "Actor id cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.createdDate = Objects.requireNonNull(createdDate, "Created date cannot be null");
        this.permissionLevel = Objects.requireNonNull(permissionLevel, "Permission level cannot be null");
    }

    public UUID getActorId() { return actorId; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public PermissionLevel getPermissionLevel() { return permissionLevel; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(actorId, actor.actorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId);
    }
}
