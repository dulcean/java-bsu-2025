package com.roulette.service;

import com.roulette.model.Ticket;
import com.roulette.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket drawRandomTicket(String teacher) {
        return ticketRepository.findRandomTicketByTeacher(teacher)
                .orElseThrow(() -> new RuntimeException("Билеты для учителя " + teacher + " не найдены"));
    }
}