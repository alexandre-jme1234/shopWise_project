package com.example.demo.domain.services;

import com.example.demo.application.dto.LoginRequest;
import com.example.demo.domain.exceptions.AuthenticationExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.infrastructure.repository.ClientRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private ClientRespository clientRespository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Connecte un client dont le compte a été créé par le commerçant.
     * Même erreur si l'email est inconnu ou si le mot de passe est faux (ne pas révéler quels comptes existent).
     */
    public Client login(LoginRequest request) {
        return clientRespository.findByEmail(request.email())
                .filter(client -> passwordEncoder.matches(request.password(), client.getPassword()))
                .orElseThrow(AuthenticationExceptions::new);
    }
}
