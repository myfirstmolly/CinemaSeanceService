package com.cinema.seance.service;

import com.cinema.seance.api.dto.HallDto;
import com.cinema.seance.api.dto.VisitorDto;
import com.cinema.seance.model.Seance;
import com.cinema.seance.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VisitorDto> rateResponse =
                restTemplate.exchange("https://localhost:8083/visitor/withdraw/"
                                + visitor.toString(),
                        HttpMethod.PUT, null, new ParameterizedTypeReference<>() {
                        });
        VisitorDto savedVisitor = rateResponse.getBody();
        if (visitor == null)
            throw new IllegalArgumentException("Balance can't be updated");
        cash += seance.getPrice();
        Ticket ticket = ticketsService.setVisitorToTicket(visitor, seance, line, place);
        return ticket;
    }

    @Override
    public double getCash() {
        return cash;
    }

    @Override
    public Seance addSeance(Seance seance) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HallDto> rateResponse =
                restTemplate.exchange("https://localhost:8083/hall/" + seance.getHallID().toString(),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        HallDto hall = rateResponse.getBody();

        if (hall == null) return null;
        Seance savedSeance = seancesRepository.save(seance);
        createTickets(seance, hall.getLinesNum(), hall.getSeatsNum());
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
