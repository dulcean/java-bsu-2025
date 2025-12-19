package bsu.famcs.nikonchik.lab2.backend.repositories.eventsrepositories;

import bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransactionRepository extends
        JpaRepository<Transaction, UUID> { }
