package bsu.famcs.nikonchik.lab2.backend.repositories.actorsrepositories;

import bsu.famcs.nikonchik.lab2.backend.entities.actors.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends
        JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
