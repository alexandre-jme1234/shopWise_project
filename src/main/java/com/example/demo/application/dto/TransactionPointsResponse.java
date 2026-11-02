package com.example.demo.application.dto;

import com.example.demo.domain.model.TransactionPoints;
import com.example.demo.domain.model.Type;

import java.time.LocalDateTime;

public record TransactionPointsResponse(Integer id, Type type, Integer points, LocalDateTime date_heure, Integer rendez_vous_id) {
    public static TransactionPointsResponse from(TransactionPoints transaction) {
        return new TransactionPointsResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getPoints(),
                transaction.getDate_heure(),
                transaction.getRendez_vous() == null ? null : transaction.getRendez_vous().getId());
    }
}
