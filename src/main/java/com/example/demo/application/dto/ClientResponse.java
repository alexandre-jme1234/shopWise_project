package com.example.demo.application.dto;

import com.example.demo.domain.model.Client;

public record ClientResponse(Integer id, String name, String email, String phone, Integer solde_points){
    public static ClientResponse from(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPhone(),
                client.getSolde_points());
    }
};
