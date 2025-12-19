package bsu.famcs.nikonchik.lab2.backend.factories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import bsu.famcs.nikonchik.lab2.backend.entities.actors.User;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.Admin;
import org.springframework.stereotype.Component;

@Component
public class ActorFactory {
    public User createUser(String username) {
        return new User(UUID.randomUUID(), username,
                LocalDateTime.now(), new ArrayList<>()
        );
    }

    public Admin createAdmin(String username) {
        return new Admin(UUID.randomUUID(), username,
                LocalDateTime.now());
    }
}
