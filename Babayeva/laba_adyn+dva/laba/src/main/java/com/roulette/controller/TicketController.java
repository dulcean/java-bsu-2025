package com.roulette.controller;

import com.roulette.model.Ticket;
import com.roulette.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public List<Ticket> getAll() {
        return ticketService.getAllTickets();
    }

    @PostMapping
    public Ticket create(@RequestBody Ticket ticket) {
        return ticketService.saveTicket(ticket);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
}