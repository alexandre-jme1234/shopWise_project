package com.example.demo.application.dto;

import com.example.demo.domain.model.RendezVous;
import com.example.demo.domain.model.Statut;

import java.time.LocalDateTime;

public record RendezVousResponse(Integer id, Integer client_id, Integer service_id, Statut statut, LocalDateTime date_heure) {
    public static RendezVousResponse from(RendezVous rendezVous) {
        return new RendezVousResponse(
                rendezVous.getId(),
                rendezVous.getClient().getId(),
                rendezVous.getService().getId(),
                rendezVous.getStatut(),
                rendezVous.getDate_heure());
    }
};
