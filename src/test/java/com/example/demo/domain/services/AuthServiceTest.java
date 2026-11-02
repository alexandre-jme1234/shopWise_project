package com.example.demo.domain.services;

import com.example.demo.application.dto.LoginRequest;
import com.example.demo.domain.exceptions.AuthenticationExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.infrastructure.repository.ClientRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private ClientRespository clientRespository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client();
        client.setId(5);
        client.setEmail("marie@shop.fr");
        client.setPassword("$2a$hash");
    }

    @Test
    public void login_identifiantsCorrects_renvoieLeClient() {
        // GIVEN
        when(clientRespository.findByEmail("marie@shop.fr")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches("motdepasse", "$2a$hash")).thenReturn(true);

        // WHEN
        Client result = authService.login(new LoginRequest("marie@shop.fr", "motdepasse"));

        // THEN
        assertEquals(client, result);
    }

    @Test
    public void login_motDePasseFaux_leveAuthenticationExceptions() {
        // GIVEN
        when(clientRespository.findByEmail("marie@shop.fr")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches("mauvais", "$2a$hash")).thenReturn(false);

        // WHEN / THEN
        assertThrows(AuthenticationExceptions.class,
                () -> authService.login(new LoginRequest("marie@shop.fr", "mauvais")));
    }

    @Test
    public void login_emailInconnu_leveAuthenticationExceptionsSansVerifierLeMotDePasse() {
        // GIVEN
        when(clientRespository.findByEmail("inconnu@shop.fr")).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(AuthenticationExceptions.class,
                () -> authService.login(new LoginRequest("inconnu@shop.fr", "motdepasse")));
        verify(passwordEncoder, never()).matches(any(), any());
    }
}
