package com.example.demo.application.controllers;

import com.example.demo.application.dto.RendezVousCreateRequest;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.exceptions.RendezVousStatutExceptions;
import com.example.demo.domain.exceptions.ShopExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.RendezVous;
import com.example.demo.domain.model.Service;
import com.example.demo.domain.model.Statut;
import com.example.demo.domain.services.RendezVousService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RendezVousController.class)
public class RendezVousControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RendezVousService rendezVousService;

    private static final String BODY = "{\"client_id\":5,\"service_id\":7,\"date_heure\":\"2099-11-10T14:30:00\"}";

    @Test
    public void createRendezVous_renvoie201() throws Exception {
        // GIVEN
        Client client = new Client();
        client.setId(5);
        Service service = new Service();
        service.setId(7);
        RendezVous created = new RendezVous(client, service, LocalDateTime.of(2099, 11, 10, 14, 30));
        created.setId(1);
        when(rendezVousService.createRendezVous(eq(1), any(RendezVousCreateRequest.class))).thenReturn(created);

        // WHEN / THEN
        mockMvc.perform(post("/shops/1/rendez-vous")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.client_id").value(5))
                .andExpect(jsonPath("$.service_id").value(7))
                .andExpect(jsonPath("$.statut").value("PLANIFIE"));
    }

    @Test
    public void createRendezVous_sansDate_renvoie400() throws Exception {
        // WHEN / THEN
        mockMvc.perform(post("/shops/1/rendez-vous")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"client_id\":5,\"service_id\":7}"))
                .andExpect(status().isBadRequest());
        verify(rendezVousService, never()).createRendezVous(any(), any());
    }

    @Test
    public void createRendezVous_clientInconnu_renvoie404() throws Exception {
        // GIVEN
        when(rendezVousService.createRendezVous(eq(1), any(RendezVousCreateRequest.class)))
                .thenThrow(new ClientExceptions(5));

        // WHEN / THEN
        mockMvc.perform(post("/shops/1/rendez-vous")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listerRendezVous_avecFiltres_renvoie200EtLaListe() throws Exception {
        // GIVEN
        Client client = new Client();
        client.setId(5);
        Service service = new Service();
        service.setId(7);
        RendezVous rdv = new RendezVous(client, service, LocalDateTime.of(2099, 11, 10, 14, 30));
        rdv.setId(1);
        when(rendezVousService.listerRendezVous(1, 5, LocalDate.of(2099, 11, 10), Statut.PLANIFIE)).thenReturn(List.of(rdv));

        // WHEN / THEN
        mockMvc.perform(get("/shops/1/rendez-vous")
                        .param("client_id", "5")
                        .param("date", "2099-11-10")
                        .param("statut", "PLANIFIE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].statut").value("PLANIFIE"));
    }

    @Test
    public void listerRendezVous_boutiqueInconnue_renvoie404() throws Exception {
        // GIVEN
        when(rendezVousService.listerRendezVous(99, null, null, null)).thenThrow(new ShopExceptions(99));

        // WHEN / THEN
        mockMvc.perform(get("/shops/99/rendez-vous"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void honorerRendezVous_renvoie200() throws Exception {
        // GIVEN
        Client client = new Client();
        client.setId(5);
        Service service = new Service();
        service.setId(7);
        RendezVous honore = new RendezVous(client, service, LocalDateTime.of(2099, 11, 10, 14, 30));
        honore.setId(1);
        honore.setStatut(Statut.HONORE);
        when(rendezVousService.honorerRendezVous(1, 1)).thenReturn(honore);

        // WHEN / THEN
        mockMvc.perform(patch("/shops/1/rendez-vous/1/honorer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("HONORE"));
    }

    @Test
    public void honorerRendezVous_dejaHonore_renvoie409() throws Exception {
        // GIVEN
        when(rendezVousService.honorerRendezVous(1, 1)).thenThrow(new RendezVousStatutExceptions(1, Statut.HONORE));

        // WHEN / THEN
        mockMvc.perform(patch("/shops/1/rendez-vous/1/honorer"))
                .andExpect(status().isConflict());
    }
}
