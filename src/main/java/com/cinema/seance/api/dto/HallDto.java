package com.cinema.seance.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HallDto {
    private UUID hallId;
    private String name;
    private int linesNum;
    private int seatsNum;

    public UUID getHallId() {
        return hallId;
    }

    public int getLinesNum() {
        return linesNum;
    }

    public int getSeatsNum() {
        return seatsNum;
    }
}
