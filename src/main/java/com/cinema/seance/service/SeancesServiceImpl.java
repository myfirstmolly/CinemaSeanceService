package com.cinema.seance.service;

import com.cinema.seance.model.Seance;
import com.cinema.seance.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public final class SeancesServiceImpl implements SeancesService {

    @Autowired
    private SeanceRepository seancesRepository;
    @Autowired
    private TicketsService ticketsService;

    private double cash;

    @Override
    public Ticket sellTicket(UUID visitor, Seance seance, int line, int place) {
        cash += seance.getPrice();
        Ticket ticket = ticketsService.setVisitorToTicket(visitor, seance, line, place);
        return ticket;
    }

    @Override
    public double getCash() {
        return cash;
    }

    @Override
    public Seance addSeance(Seance seance, int lines, int seats) {
        Seance savedSeance = seancesRepository.save(seance);
        createTickets(seance, lines, seats);
        return savedSeance;
    }

    public void createTickets(Seance seance, int lines, int seats) {
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < seats; j++) {
                Ticket ticket = new Ticket(seance, i + 1, j + 1);
                ticketsService.addTicket(ticket);
            }
        }
    }

    @Override
    public List<Seance> getAllSeances() {
        return seancesRepository.findAll();
    }

    @Override
    public Seance getSeanceById(UUID id) {
        return seancesRepository.findById(id).get();
    }

    @Override
    public void deleteSeanceById(UUID id) {
        ticketsService.deleteAllBySeance(seancesRepository.findById(id).get());
        seancesRepository.deleteById(id);
    }

    @Override
    public void deleteAllSeancesByFilm(UUID film) {
        seancesRepository.deleteAllByFilmID(film);
    }

}
