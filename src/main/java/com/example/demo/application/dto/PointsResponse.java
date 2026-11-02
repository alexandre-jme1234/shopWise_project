package com.example.demo.application.dto;

import java.util.List;

/** Solde de points d'un client et historique de ses transactions (la plus récente en premier). */
public record PointsResponse(Integer client_id, Integer solde_points, List<TransactionPointsResponse> transactions) {}
