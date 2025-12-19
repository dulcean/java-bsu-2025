package bsu.famcs.nikonchik.lab2.backend.repositories.eventsrepositories;

import bsu.famcs.nikonchik.lab2.backend.entities.events.lifecycleevents.AccountFreezeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface AccountFreezeActionsRepository extends
        JpaRepository<AccountFreezeEvent, UUID> {
    List<AccountFreezeEvent> findByAccountId(UUID accountId);
}
