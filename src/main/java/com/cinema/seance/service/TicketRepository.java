package com.cinema.seance.service;

import com.cinema.seance.model.Seance;
import com.cinema.seance.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    void deleteAllBySeance(Seance seance);
    Ticket getBySeanceAndLineAndSeat(Seance seance, int line, int seat);
}
