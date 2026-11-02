package com.example.demo.domain.services;

import com.example.demo.application.dto.PointsResponse;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.RendezVous;
import com.example.demo.domain.model.TransactionPoints;
import com.example.demo.domain.model.Type;
import com.example.demo.infrastructure.repository.ClientRespository;
import com.example.demo.infrastructure.repository.TransactionPointsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FideliteServiceTest {

    @InjectMocks
    private FideliteService fideliteService;

    @Mock
    private ClientRespository clientRespository;

    @Mock
    private TransactionPointsRepository transactionPointsRepository;

    @Test
    public void consulterPoints_clientExistant_renvoieSoldeEtHistorique() {
        // GIVEN
        Client client = new Client();
        client.setId(5);
        client.setSolde_points(30);
        RendezVous rdv = new RendezVous();
        rdv.setId(9);
        TransactionPoints gain = new TransactionPoints(client, rdv, LocalDateTime.of(2026, 11, 2, 10, 0), Type.GAIN, 50);
        TransactionPoints utilisation = new TransactionPoints(client, null, LocalDateTime.of(2026, 11, 2, 11, 0), Type.UTILISATION, 20);
        when(clientRespository.findById(5)).thenReturn(Optional.of(client));
        when(transactionPointsRepository.findByClientIdOrderByIdDesc(5)).thenReturn(List.of(utilisation, gain));

        // WHEN
        PointsResponse result = fideliteService.consulterPoints(5);

        // THEN
        assertEquals(5, result.client_id());
        assertEquals(30, result.solde_points());
        assertEquals(2, result.transactions().size());
        assertEquals(Type.UTILISATION, result.transactions().get(0).type());
        assertNull(result.transactions().get(0).rendez_vous_id());
        assertEquals(9, result.transactions().get(1).rendez_vous_id());
    }

    @Test
    public void consulterPoints_clientInconnu_leveClientExceptions() {
        // GIVEN
        when(clientRespository.findById(99)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(ClientExceptions.class, () -> fideliteService.consulterPoints(99));
        verify(transactionPointsRepository, never()).findByClientIdOrderByIdDesc(any());
    }
}
