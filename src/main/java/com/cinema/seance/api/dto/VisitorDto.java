package com.cinema.seance.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitorDto {
    private UUID userId;
    private String name;
    private double money;
    private int age;
}
