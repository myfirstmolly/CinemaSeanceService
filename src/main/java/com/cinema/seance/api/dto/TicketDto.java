package com.cinema.seance.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TicketDto {
    UUID visitor;
    int line;
    int place;
}
