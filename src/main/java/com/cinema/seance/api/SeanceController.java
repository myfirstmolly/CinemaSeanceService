package com.cinema.seance.api;

import com.cinema.seance.api.dto.HallDto;
import com.cinema.seance.api.dto.TicketDto;
import com.cinema.seance.api.dto.VisitorDto;
import com.cinema.seance.model.Seance;
import com.cinema.seance.model.Ticket;
import com.cinema.seance.service.SeancesService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/seance")
@AllArgsConstructor
@NoArgsConstructor
public class SeanceController {

    @Autowired
    private SeancesService seancesService;

    @PostMapping
    public Seance addSeance(@RequestBody Seance seance) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HallDto> rateResponse =
                restTemplate.exchange("https://localhost:8083/hall/" + seance.getHallID().toString(),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        HallDto hall = rateResponse.getBody();
        if(hall == null) return null;
        return seancesService.addSeance(seance, hall.getLinesNum(), hall.getSeatsNum());
    }

    @PostMapping("{seanceId}/visitor")
    public Ticket buyTicket(@PathVariable(value = "seanceId") UUID id,
                            @RequestBody(required = false) TicketDto ticketRequest) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VisitorDto> rateResponse =
                restTemplate.exchange("https://localhost:8083/visitor/withdraw/"
                                + ticketRequest.getVisitor().toString(),
                        HttpMethod.PUT, null, new ParameterizedTypeReference<>() {
                        });
        VisitorDto visitor = rateResponse.getBody();
        if (visitor == null)
            throw new IllegalArgumentException("Balance can't be updated");
        return seancesService.sellTicket(ticketRequest.getVisitor(),
                seancesService.getSeanceById(id), ticketRequest.getLine(),
                ticketRequest.getPlace());
    }

    @GetMapping
    public List<Seance> getAll() {
        return seancesService.getAllSeances();
    }

    @GetMapping("{seanceId}")
    public Seance getById(@PathVariable(value = "seanceId") UUID id) {
        return seancesService.getSeanceById(id);
    }

    @DeleteMapping("{seanceId}")
    public ResponseEntity<Void> deleteSeance(@PathVariable(value = "seanceId") UUID id) {
        seancesService.deleteSeanceById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("delete-by-film/{filmId}")
    public ResponseEntity<Void> deleteSeancesByFilmId(@PathVariable(value = "filmId") UUID id) {
        seancesService.deleteAllSeancesByFilm(id);
        return ResponseEntity.noContent().build();
    }

}
