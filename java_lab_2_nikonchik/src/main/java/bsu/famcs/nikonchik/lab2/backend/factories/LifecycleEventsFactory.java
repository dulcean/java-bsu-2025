package bsu.famcs.nikonchik.lab2.backend.factories;

import bsu.famcs.nikonchik.lab2.backend.entities.events.Event.EventStatus;
import bsu.famcs.nikonchik.lab2.backend.entities.events.lifecycleevents.AccountFreezeEvent;
import bsu.famcs.nikonchik.lab2.backend.entities.events.lifecycleevents.AccountFreezeEvent.ActionType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class LifecycleEventsFactory {
    public AccountFreezeEvent createAccountFreezeEvent(UUID accountId, UUID initiatorId,
                                                              String description, ActionType actionType) {
        return new AccountFreezeEvent(
                UUID.randomUUID(),
                accountId,
                initiatorId,
                LocalDateTime.now(),
                EventStatus.PENDING,
                description,
                actionType
        );
    }
}
