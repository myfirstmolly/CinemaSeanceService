package com.cinema.seance.model;

import com.cinema.seance.SeanceResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EnableAutoConfiguration
@Entity
@Data
public final class Seance {

    @Id
    private UUID seanceId;
    private String seanceDate;
    private double price;
    private UUID filmId;
    private UUID hallId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "seance")
    private List<Ticket> ticket;

    public Seance() {
        this.seanceId = UUID.randomUUID();
        ticket = new ArrayList<>();
    }

    public Seance(UUID id, String date, double price, UUID filmId, UUID hallId) {
        seanceId = id;
        this.seanceDate = date;
        this.price = price;
        this.filmId = filmId;
        this.hallId = hallId;
        ticket = new ArrayList<>();
    }

    public SeanceResponse toSeanceResponse() {
        return SeanceResponse.newBuilder().
                setId(filmId.toString()).
                setSeanceDate(seanceDate).
                setPrice(price).
                setFilmId(filmId.toString()).
                setHallId(hallId.toString()).
                build();
    }

    public UUID getSeanceId() {
        return seanceId;
    }

    public String getSeanceDate() {
        return seanceDate;
    }

    public double getPrice() {
        return price;
    }

    public UUID getFilmId() {
        return filmId;
    }

    public UUID getHallId() {
        return hallId;
    }

    public List<Ticket> getTicket() {
        return ticket;
    }
}
