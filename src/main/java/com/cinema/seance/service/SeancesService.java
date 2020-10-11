package com.cinema.seance.service;

import com.cinema.seance.model.Seance;
import com.cinema.seance.model.Ticket;

import java.util.List;
import java.util.UUID;

public interface SeancesService {
    Ticket sellTicket(UUID visitor, Seance seance, int line, int place);

    double getCash();

    Seance addSeance(Seance seance, int lines, int seats);

    List<Seance> getAllSeances();

    Seance getSeanceById(UUID id);

    void deleteSeanceById(UUID id);

    void deleteAllSeancesByFilm(UUID film);
}
