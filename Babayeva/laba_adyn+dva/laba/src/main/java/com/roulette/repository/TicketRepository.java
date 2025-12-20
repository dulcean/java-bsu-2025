package com.roulette.repository;

import com.roulette.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "SELECT * FROM tickets WHERE teacher_name = :teacher ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Ticket> findRandomTicketByTeacher(String teacher);
}