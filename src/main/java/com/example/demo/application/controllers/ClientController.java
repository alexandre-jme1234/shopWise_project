package com.example.demo.application.controllers;


import com.example.demo.application.dto.ClientResponse;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    public ClientService clientService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable("id") Integer id) {
        Client client = clientService.findById(id)
                .orElseThrow(() -> new ClientExceptions(id));
        return ResponseEntity.ok(ClientResponse.from(client));
    };


    @PutMapping(value = "/{id}")
    public Client clientUpdate(@PathVariable("id") Integer id, @RequestBody Client client) {
        System.out.println(client);
        return clientService.updateOrInsertClient(client);
    }
}
