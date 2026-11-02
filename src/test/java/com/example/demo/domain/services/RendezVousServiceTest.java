package com.example.demo.domain.services;

import com.example.demo.application.dto.RendezVousCreateRequest;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.exceptions.ServiceExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.RendezVous;
import com.example.demo.domain.model.Service;
import com.example.demo.domain.model.Shop;
import com.example.demo.domain.model.Statut;
import com.example.demo.infrastructure.repository.ClientRespository;
import com.example.demo.infrastructure.repository.RendezVousRespository;
import com.example.demo.infrastructure.repository.ServiceRepository;
import com.example.demo.infrastructure.repository.TransactionPointsRepository;
import com.example.demo.domain.model.TransactionPoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RendezVousServiceTest {

    @InjectMocks
    private RendezVousService rendezVousService;

    @Mock
    private RendezVousRespository rendezVousRespository;

    @Mock
    private ClientRespository clientRespository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ShopService shopService;

    @Mock
    private TransactionPointsRepository transactionPointsRepository;

    private Shop shop;
    private Shop autreShop;
    private Client client;
    private Service service;
    private final LocalDateTime dateHeure = LocalDateTime.now().plusDays(3);

    @BeforeEach
    public void setUp() {
        shop = new Shop("Orange", "Orange");
        shop.setId(1);
        autreShop = new Shop("Lyon", "Lyon");
        autreShop.setId(2);

        client = new Client();
        client.setId(5);
        client.setShop(shop);

        service = new Service();
        service.setId(7);
        service.setShop(shop);

        when(shopService.getShopById(1)).thenReturn(Optional.of(shop));
    }

    @Test
    public void createRendezVous_cheminNominal_statutPlanifie() {
        // GIVEN
        when(clientRespository.findById(5)).thenReturn(Optional.of(client));
        when(serviceRepository.findById(7)).thenReturn(Optional.of(service));
        when(rendezVousRespository.save(any(RendezVous.class))).thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        RendezVous result = rendezVousService.createRendezVous(1, new RendezVousCreateRequest(5, 7, dateHeure));

        // THEN
        assertEquals(client, result.getClient());
        assertEquals(service, result.getService());
        assertEquals(dateHeure, result.getDate_heure());
        assertEquals(Statut.PLANIFIE, result.getStatut());
        verify(rendezVousRespository).save(any(RendezVous.class));
    }

    @Test
    public void createRendezVous_clientInconnu_leveClientExceptions() {
        // GIVEN
        when(clientRespository.findById(99)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(ClientExceptions.class,
                () -> rendezVousService.createRendezVous(1, new RendezVousCreateRequest(99, 7, dateHeure)));
        verify(rendezVousRespository, never()).save(any(RendezVous.class));
    }

    @Test
    public void createRendezVous_clientDUnAutreShop_leveClientExceptions() {
        // GIVEN
        client.setShop(autreShop);
        when(clientRespository.findById(5)).thenReturn(Optional.of(client));

        // WHEN / THEN
        assertThrows(ClientExceptions.class,
                () -> rendezVousService.createRendezVous(1, new RendezVousCreateRequest(5, 7, dateHeure)));
        verify(rendezVousRespository, never()).save(any(RendezVous.class));
    }

    @Test
    public void createRendezVous_serviceInconnu_leveServiceExceptions() {
        // GIVEN
        when(clientRespository.findById(5)).thenReturn(Optional.of(client));
        when(serviceRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(ServiceExceptions.class,
                () -> rendezVousService.createRendezVous(1, new RendezVousCreateRequest(5, 99, dateHeure)));
        verify(rendezVousRespository, never()).save(any(RendezVous.class));
    }

    @Test
    public void createRendezVous_serviceDUnAutreShop_leveServiceExceptions() {
        // GIVEN
        service.setShop(autreShop);
        when(clientRespository.findById(5)).thenReturn(Optional.of(client));
        when(serviceRepository.findById(7)).thenReturn(Optional.of(service));

        // WHEN / THEN
        assertThrows(ServiceExceptions.class,
                () -> rendezVousService.createRendezVous(1, new RendezVousCreateRequest(5, 7, dateHeure)));
        verify(rendezVousRespository, never()).save(any(RendezVous.class));
    }

    @Test
    public void honorerRendezVous_planifie_passeHonoreEtCrediteLesPointsDuService() {
        // GIVEN (données) : un client à 0 point, un service à 15 points, un rendez-vous PLANIFIE dans la boutique 1
        client.setSolde_points(0);
        service.setPoints(15);
        RendezVous rendezVous = new RendezVous(client, service, dateHeure);   // statut PLANIFIE par défaut
        rendezVous.setId(10);

        // GIVEN (répliques des figurants) : uniquement ce que le service lit
        when(rendezVousRespository.findById(10)).thenReturn(Optional.of(rendezVous));
        when(rendezVousRespository.save(any(RendezVous.class))).thenAnswer(inv -> inv.getArgument(0));

        // WHEN : on joue la scène, un seul appel
        RendezVous result = rendezVousService.honorerRendezVous(1, 10);

        // THEN : ce que l'acteur a fait
        assertEquals(Statut.HONORE, result.getStatut());
        assertEquals(15, client.getSolde_points());
        verify(transactionPointsRepository).save(any(TransactionPoints.class));
        verify(clientRespository).save(client);
    }
}
