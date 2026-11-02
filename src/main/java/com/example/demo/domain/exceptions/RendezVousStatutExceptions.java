package com.example.demo.domain.exceptions;

import com.example.demo.domain.model.Statut;

public class RendezVousStatutExceptions extends RuntimeException {
    public RendezVousStatutExceptions(Integer id, Statut statut) {
        super("RendezVous " + id + " déjà " + statut + " : seul un rendez-vous PLANIFIE peut être honoré");
    }
};
