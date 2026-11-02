package com.example.demo.domain.services;

import com.example.demo.application.dto.ClientCreateRequest;
import com.example.demo.application.dto.ClientResponse;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.exceptions.ShopExceptions;
import com.example.demo.domain.exceptions.UserExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.Shop;
import com.example.demo.domain.model.User;
import com.example.demo.infrastructure.repository.ClientRespository;
import com.example.demo.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.Character.getName;

@Service
public class ClientService {

    @Autowired
    public ClientRespository clientRespository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ShopService shopService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * upadate Client
     *
     * @param client
     * @return
     */
    public Client updateOrInsertClient(Client client) {
        return clientRespository.updateOrInsert(client);
    }

    public Optional<Client> findById(Integer id){
        return clientRespository.findById(id);
     };

    @Transactional
    public ClientResponse createClient(ClientCreateRequest client, Integer shop_id) {
        Shop shop = shopService.getShopById(shop_id)
                .orElseThrow(() -> new ShopExceptions(shop_id));

        if(userRepository.existsByEmail(client.email())) {
            throw new UserExceptions("user already exist");
        }

        Client new_client = new Client();

        new_client.setName(client.name());
        new_client.setPhone(client.phone());
        new_client.setEmail(client.email());
        new_client.setShop(shop);
        new_client.setSolde_points(0);
        new_client.setPassword(passwordEncoder.encode(client.password()));

        Client saved = clientRespository.save(new_client);
        return ClientResponse.from(saved);
    };

    @Transactional
    public Client updateClient(Integer client_id, Client client_update, Integer shop_id) {
        Shop shop = shopService.getShopById(shop_id)
                .orElseThrow(() -> new ShopExceptions(shop_id));

        Client existant = clientRespository.findById(client_id)
                .orElseThrow(() -> new ClientExceptions(client_id));

        if (!existant.getShop().getId().equals(shop.getId())) {
            throw new ClientExceptions(client_id);
        }

        existant.setEmail(client_update.getEmail());
        existant.setPhone(client_update.getPhone());
        existant.setName(client_update.getName());

        return clientRespository.updateOrInsert(existant);
    }
};

