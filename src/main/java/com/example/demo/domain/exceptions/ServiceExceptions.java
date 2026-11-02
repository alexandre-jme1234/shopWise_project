package com.example.demo.domain.exceptions;

public class ServiceExceptions extends RuntimeException {
    public ServiceExceptions(Integer id) {
        super("Service " + id + " introuvable");
    }
};
