package com.cinema.seance.service;

import com.cinema.film.FilmByNameRequest;
import com.cinema.film.FilmResponse;
import com.cinema.film.FilmServiceGrpc;
import com.cinema.hall.HallByNameRequest;
import com.cinema.hall.HallResponse;
import com.cinema.hall.HallServiceGrpc;
import com.cinema.seance.SeanceRequest;
import com.cinema.seance.api.dto.HallDto;
import com.cinema.seance.api.dto.VisitorDto;
import com.cinema.seance.api.dto.WithdrawDto;
import com.cinema.seance.model.Seance;
import com.cinema.seance.model.Ticket;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        WithdrawDto withdrawDto = new WithdrawDto(seance.getPrice());
        HttpEntity<WithdrawDto> entity = new HttpEntity<>(withdrawDto, headers);
        ResponseEntity<VisitorDto> response =
                restTemplate.exchange("http://cinema-visitors:8084/visitor/withdraw/" + visitor.toString(),
                        HttpMethod.PUT,
                        entity,
                        VisitorDto.class);

        cash += seance.getPrice();
        Ticket ticket = ticketsService.setVisitorToTicket(visitor, seance, line, place);
        return ticket;
    }

    @Override
    public Seance addSeanceGrpc(SeanceRequest request) {
        String url = "localhost";
        ManagedChannel hallChannel = ManagedChannelBuilder.forAddress(url, 7083).usePlaintext().build();
        HallServiceGrpc.HallServiceBlockingStub hallStub = HallServiceGrpc.newBlockingStub(hallChannel);
        HallByNameRequest hall = HallByNameRequest.newBuilder().
                setName(request.getHall().getName()).
                build();
        HallResponse hallResponse = hallStub.byName(hall);

        ManagedChannel filmChannel = ManagedChannelBuilder.forAddress(url, 7081).usePlaintext().build();
        FilmServiceGrpc.FilmServiceBlockingStub filmStub = FilmServiceGrpc.newBlockingStub(filmChannel);
        FilmByNameRequest film = FilmByNameRequest.newBuilder().
                setName(request.getFilm().getName()).
                build();
        FilmResponse filmResponse = filmStub.byName(film);

        Seance seance = new Seance(UUID.randomUUID(),
                request.getSeanceDate(),
                request.getPrice(),
                UUID.fromString(filmResponse.getId()),
                UUID.fromString(hallResponse.getId()));

        Seance savedSeance = seancesRepository.save(seance);
        createTickets(seance, hallResponse.getLinesNum(), hallResponse.getSeatsNum());
        return savedSeance;
    }

    @Override
    public Seance addSeance(Seance seance) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HallDto> rateResponse =
                restTemplate.exchange("http://cinema-halls:8083/hall/" + seance.getHallId().toString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        HallDto hall = rateResponse.getBody();
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
        seancesRepository.deleteAllByFilmId(film);
    }

}
