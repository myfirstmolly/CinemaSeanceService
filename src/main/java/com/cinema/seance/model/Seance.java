package com.cinema.seance.model;

import com.cinema.seance.SeanceResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EnableAutoConfiguration
public final class Seance {

    @Id
    private UUID seanceId;
    private String seanceDate;
    private double price;
    private UUID hallId;
    private UUID filmId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "seance")
    private List<Ticket> ticket;

    public Seance(UUID id, String date, double price, UUID hallId, UUID filmId) {
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
}
