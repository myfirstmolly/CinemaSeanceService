package com.cinema.seance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public final class Seance {

    @Id
    private UUID seanceId;

    private String seanceDate;

    private double price;

    private UUID filmID;

    private UUID hallID;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "seance")
    private List<Ticket> ticket;

    public Seance(String date, double price, UUID filmId, UUID hallId) {
        seanceId = UUID.randomUUID();
        this.seanceDate = date;
        this.price = price;
        this.filmID = filmId;
        this.hallID = hallId;
        ticket = new ArrayList<>();
    }
}
