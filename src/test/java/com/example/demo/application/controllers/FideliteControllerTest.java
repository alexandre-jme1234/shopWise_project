package com.example.demo.application.controllers;

import com.example.demo.application.dto.PointsResponse;
import com.example.demo.application.dto.TransactionPointsResponse;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.model.Type;
import com.example.demo.domain.services.FideliteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FideliteController.class)
public class FideliteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FideliteService fideliteService;

    @Test
    public void consulterPoints_renvoie200AvecSoldeEtHistorique() throws Exception {
        // GIVEN
        PointsResponse points = new PointsResponse(5, 50, List.of(
                new TransactionPointsResponse(1, Type.GAIN, 50, LocalDateTime.of(2026, 11, 2, 10, 0), 9)));
        when(fideliteService.consulterPoints(5)).thenReturn(points);

        // WHEN / THEN
        mockMvc.perform(get("/clients/5/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde_points").value(50))
                .andExpect(jsonPath("$.transactions.length()").value(1))
                .andExpect(jsonPath("$.transactions[0].type").value("GAIN"))
                .andExpect(jsonPath("$.transactions[0].rendez_vous_id").value(9));
    }

    @Test
    public void consulterPoints_clientInconnu_renvoie404() throws Exception {
        // GIVEN
        when(fideliteService.consulterPoints(99)).thenThrow(new ClientExceptions(99));

        // WHEN / THEN
        mockMvc.perform(get("/clients/99/points"))
                .andExpect(status().isNotFound());
    }
}
