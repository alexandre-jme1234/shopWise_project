package com.example.demo.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



public class ClientExceptions extends RuntimeException {
    public ClientExceptions(Integer id) {
        super("Client " + id + " introuvable");
    }
}
