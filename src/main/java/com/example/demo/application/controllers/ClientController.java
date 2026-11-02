package com.example.demo.application.controllers;


import com.example.demo.application.dto.ClientCreateRequest;
import com.example.demo.application.dto.ClientResponse;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.exceptions.UserExceptions;
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

    @PostMapping(value = "/shops/{shop_id}")
    public ClientResponse createClient(@PathVariable("shop_id") Integer shop_id, @RequestBody ClientCreateRequest client_request){
        return clientService.createClient(client_request, shop_id);
    };

    @PatchMapping("/{id}/shops/{shop_id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable("id") Integer id,
                                            @PathVariable("shop_id") Integer shop_id,
                                            @RequestBody Client client_update) {
        Client updated = clientService.updateClient(id , client_update, shop_id);
        return ResponseEntity.ok(ClientResponse.from(updated));
    }

}
