package com.cinema.seance.service;

import com.cinema.seance.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, UUID> {
    void deleteAllByFilmId(UUID film);
}
