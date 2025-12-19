package bsu.famcs.nikonchik.lab2.backend.entities.actors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "admins")
public class Admin extends Actor implements Comparable<Admin> {
    protected Admin() {
        super();
    }

    public Admin(UUID actorId, String username, LocalDateTime createdDate) {
        super(actorId, username, createdDate, PermissionLevel.ADMIN);
    }

    @Override
    public int compareTo(Admin other) {
        return this.username.compareTo(other.username);
    }

    @Override
    public String toString() {
        return String.format("Admin{id='%s', username='%s', created=%s}",
                actorId, username, createdDate);
    }
}
