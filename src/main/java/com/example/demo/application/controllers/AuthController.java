package com.example.demo.application.controllers;

import com.example.demo.application.dto.ClientResponse;
import com.example.demo.application.dto.LoginRequest;
import com.example.demo.domain.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<ClientResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ClientResponse.from(authService.login(request)));
    }
}
