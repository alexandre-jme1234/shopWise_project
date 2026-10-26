package com.example.demo.domain.exceptions;

import com.example.demo.domain.model.User;

public class UserExceptions extends RuntimeException {
    public UserExceptions(String message) {
        super(message);
    }
}
