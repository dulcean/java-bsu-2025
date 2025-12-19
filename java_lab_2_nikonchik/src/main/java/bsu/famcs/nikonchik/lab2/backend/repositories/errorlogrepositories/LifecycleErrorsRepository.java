package bsu.famcs.nikonchik.lab2.backend.repositories.errorlogrepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import bsu.famcs.nikonchik.lab2.backend.entities.logs.lifecycleerrorlogs.FreezeErrorLog;

public interface LifecycleErrorsRepository extends
        JpaRepository<FreezeErrorLog, UUID> {
    Optional<FreezeErrorLog> findByInitiatorId(UUID initiatorId);
    Optional<FreezeErrorLog> findByEventId(UUID eventId);
    List<FreezeErrorLog> findByActionType(String actionType);
}
