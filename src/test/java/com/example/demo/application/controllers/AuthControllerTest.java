package com.example.demo.application.controllers;

import com.example.demo.application.dto.LoginRequest;
import com.example.demo.domain.exceptions.AuthenticationExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    private static final String BODY = "{\"email\":\"marie@shop.fr\",\"password\":\"motdepasse\"}";

    @Test
    public void login_identifiantsCorrects_renvoie200EtLeProfilSansMotDePasse() throws Exception {
        // GIVEN
        Client client = new Client();
        client.setId(5);
        client.setEmail("marie@shop.fr");
        client.setSolde_points(30);
        when(authService.login(any(LoginRequest.class))).thenReturn(client);

        // WHEN / THEN
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.solde_points").value(30))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void login_identifiantsFaux_renvoie401() throws Exception {
        // GIVEN
        when(authService.login(any(LoginRequest.class))).thenThrow(new AuthenticationExceptions());

        // WHEN / THEN
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void login_sansMotDePasse_renvoie400() throws Exception {
        // WHEN / THEN
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"marie@shop.fr\"}"))
                .andExpect(status().isBadRequest());
        verify(authService, never()).login(any());
    }
}
