package com.roulette.controller;

import com.roulette.model.Ticket;
import com.roulette.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Обязательно для {teacher}
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping("/draw/{teacher}")
    public Ticket drawRandomTicket(@PathVariable String teacher) {
    return ticketRepository.findRandomTicketByTeacher(teacher)
            .orElse(null); 
}
}