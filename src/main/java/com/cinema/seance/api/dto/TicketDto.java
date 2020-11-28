package com.cinema.seance.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketDto {
    UUID visitor;
    int line;
    int place;

    public UUID getVisitor() {
        return visitor;
    }

    public int getLine() {
        return line;
    }

    public int getPlace() {
        return place;
    }
}
