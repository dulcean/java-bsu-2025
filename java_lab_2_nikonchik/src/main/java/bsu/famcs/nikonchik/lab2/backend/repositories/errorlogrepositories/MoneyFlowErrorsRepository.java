package bsu.famcs.nikonchik.lab2.backend.repositories.errorlogrepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import bsu.famcs.nikonchik.lab2.backend.entities.logs.moneyflowerrorlogs.MoneyFlowErrorLog;

public interface MoneyFlowErrorsRepository extends
        JpaRepository<MoneyFlowErrorLog, UUID> {
    Optional<MoneyFlowErrorLog> findByInitiatorId(UUID initiatorId);
    Optional<MoneyFlowErrorLog> findByEventId(UUID eventId);
    List<MoneyFlowErrorLog> findByTransactionType(String transactionType);
}
