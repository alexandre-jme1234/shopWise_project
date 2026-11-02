package com.example.demo.application.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RendezVousCreateRequest(
        @NotNull
        Integer client_id,

        @NotNull
        Integer service_id,

        @NotNull @Future                        // un rendez-vous se prend dans le futur
        LocalDateTime date_heure                // format ISO : "2026-11-10T14:30:00"
)
{}
