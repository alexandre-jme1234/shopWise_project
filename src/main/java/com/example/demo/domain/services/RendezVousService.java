package com.example.demo.domain.services;

import com.example.demo.application.dto.RendezVousCreateRequest;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.exceptions.RendezVousExceptions;
import com.example.demo.domain.exceptions.RendezVousStatutExceptions;
import com.example.demo.domain.exceptions.ServiceExceptions;
import com.example.demo.domain.exceptions.ShopExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.RendezVous;
import com.example.demo.domain.model.Shop;
import com.example.demo.domain.model.Statut;
import com.example.demo.domain.model.TransactionPoints;
import com.example.demo.domain.model.Type;
import com.example.demo.infrastructure.repository.ClientRespository;
import com.example.demo.infrastructure.repository.RendezVousRespository;
import com.example.demo.infrastructure.repository.ServiceRepository;
import com.example.demo.infrastructure.repository.TransactionPointsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RendezVousService {

    @Autowired
    public RendezVousRespository rendezVousRespository;

    @Autowired
    public ClientRespository clientRespository;

    @Autowired
    public ServiceRepository serviceRepository;

    @Autowired
    public ShopService shopService;

    @Autowired
    public TransactionPointsRepository transactionPointsRepository;

    /**
     * Crée un rendez-vous (statut PLANIFIE) pour un client et un service de la boutique.
     */
    @Transactional
    public RendezVous createRendezVous(Integer shop_id, RendezVousCreateRequest request) {
        Shop shop = shopService.getShopById(shop_id)
                .orElseThrow(() -> new ShopExceptions(shop_id));

        Client client = clientRespository.findById(request.client_id())
                .orElseThrow(() -> new ClientExceptions(request.client_id()));
        if (!client.getShop().getId().equals(shop.getId())) {
            throw new ClientExceptions(request.client_id());
        }

        com.example.demo.domain.model.Service service = serviceRepository.findById(request.service_id())
                .orElseThrow(() -> new ServiceExceptions(request.service_id()));
        if (!service.getShop().getId().equals(shop.getId())) {
            throw new ServiceExceptions(request.service_id());
        }

        return rendezVousRespository.save(new RendezVous(client, service, request.date_heure()));
    };

    /**
     * Passe un rendez-vous PLANIFIE à HONORE et crédite le client des points du service (trace dans TransactionPoints).
     */
    @Transactional
    public RendezVous honorerRendezVous(Integer shop_id, Integer rendez_vous_id) {
        Shop shop = shopService.getShopById(shop_id)
                .orElseThrow(() -> new ShopExceptions(shop_id));

        RendezVous rendezVous = rendezVousRespository.findById(rendez_vous_id)
                .orElseThrow(() -> new RendezVousExceptions(rendez_vous_id));
        if (!rendezVous.getService().getShop().getId().equals(shop.getId())) {
            throw new RendezVousExceptions(rendez_vous_id);
        }
        if (rendezVous.getStatut() != Statut.PLANIFIE) {
            throw new RendezVousStatutExceptions(rendez_vous_id, rendezVous.getStatut());
        }

        rendezVous.setStatut(Statut.HONORE);
        Client client = rendezVous.getClient();
        int points = rendezVous.getService().getPoints();
        client.crediter(points);

        transactionPointsRepository.save(
                new TransactionPoints(client, rendezVous, LocalDateTime.now(), Type.GAIN, points));
        clientRespository.save(client);
        return rendezVousRespository.save(rendezVous);
    };
}
