package bsu.famcs.nikonchik.lab2.backend.repositories.actorsrepositories;

import bsu.famcs.nikonchik.lab2.backend.entities.actors.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AdminsRepository extends
        JpaRepository<Admin, UUID> {
    Optional<Admin> findByUsername(String username);
}
