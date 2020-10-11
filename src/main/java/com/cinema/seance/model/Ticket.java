package com.cinema.seance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@EnableAutoConfiguration
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public final class Ticket {

    @Id
    private UUID ticketId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "seance_id")
    private Seance seance;

    private int line;
    private int seat;

    private UUID visitorId;

    public Ticket(Seance seance, int line, int seat) {
        ticketId = UUID.randomUUID();
        this.seance = seance;
        this.line = line;
        this.seat = seat;
    }

    public Ticket setVisitorId(UUID visitorId) {
        this.visitorId = visitorId;
        return this;
    }

}
