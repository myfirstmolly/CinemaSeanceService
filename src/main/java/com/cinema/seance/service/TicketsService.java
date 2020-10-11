package com.cinema.seance.service;

import com.cinema.seance.model.Seance;
import com.cinema.seance.model.Ticket;

import java.util.List;
import java.util.UUID;

public interface TicketsService {
    Ticket addTicket(Ticket ticket);

    List<Ticket> getAll();

    Ticket getById(UUID id);

    Ticket setVisitorToTicket(UUID visitor, Seance seance, int line, int seat);

    void deleteAllBySeance(Seance seance);

    Ticket getBySeanceAndLineAndSeat(Seance seance, int line, int seat);
}
