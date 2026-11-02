package com.example.demo.domain.exceptions;

public class RendezVousExceptions extends RuntimeException {
    public RendezVousExceptions(Integer id) {
        super("RendezVous " + id + " introuvable");
    }
};
