package com.example.demo.application.controllers;

import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.services.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Test
    public void updateClient_renvoie200EtLeClientModifie() throws Exception {
        // GIVEN
        Client updated = new Client();
        updated.setId(5);
        updated.setEmail("new@test.com");
        when(clientService.updateClient(eq(5), any(Client.class), eq(1))).thenReturn(updated);

        // WHEN / THEN
        mockMvc.perform(patch("/clients/5/shops/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"new@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.email").value("new@test.com"));
    }

    @Test
    public void updateClient_clientInconnu_renvoie404() throws Exception {
        // GIVEN
        when(clientService.updateClient(eq(99), any(Client.class), eq(1))).thenThrow(new ClientExceptions(99));

        // WHEN / THEN
        mockMvc.perform(patch("/clients/99/shops/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"new@test.com\"}"))
                .andExpect(status().isNotFound());
    }
}
