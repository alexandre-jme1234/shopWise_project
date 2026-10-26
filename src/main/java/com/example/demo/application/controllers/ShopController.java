package com.example.demo.application.controllers;

import com.example.demo.application.dto.ClientCreateRequest;
import com.example.demo.application.dto.ClientResponse;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.exceptions.ShopExceptions;
import com.example.demo.domain.exceptions.UserExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.Shop;
import com.example.demo.domain.services.ClientService;
import com.example.demo.domain.services.ShopService;
import com.example.demo.domain.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/shops")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/{id}/clients")
    public List<ClientResponse> getListClientsByShop(@PathVariable("id") Integer id) {
        return shopService.getClientsByShop(id)
                .stream()
                .map(ClientResponse::from)
                .toList();
    };
}
