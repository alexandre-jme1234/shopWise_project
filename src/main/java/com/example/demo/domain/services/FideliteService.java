package com.example.demo.domain.services;

import com.example.demo.application.dto.PointsResponse;
import com.example.demo.application.dto.TransactionPointsResponse;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.infrastructure.repository.ClientRespository;
import com.example.demo.infrastructure.repository.TransactionPointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FideliteService {

    @Autowired
    private ClientRespository clientRespository;

    @Autowired
    private TransactionPointsRepository transactionPointsRepository;

    /**
     * Solde de points du client et historique de ses transactions (la plus récente en premier).
     */
    @Transactional(readOnly = true)
    public PointsResponse consulterPoints(Integer client_id) {
        Client client = clientRespository.findById(client_id)
                .orElseThrow(() -> new ClientExceptions(client_id));

        List<TransactionPointsResponse> transactions = transactionPointsRepository.findByClientIdOrderByIdDesc(client_id)
                .stream().map(TransactionPointsResponse::from).toList();

        return new PointsResponse(client.getId(), client.getSolde_points(), transactions);
    }
}
