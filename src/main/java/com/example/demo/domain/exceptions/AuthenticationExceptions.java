package com.example.demo.domain.exceptions;

public class AuthenticationExceptions extends RuntimeException {
    public AuthenticationExceptions() {
        super("email ou mot de passe incorrect");
    }
}
