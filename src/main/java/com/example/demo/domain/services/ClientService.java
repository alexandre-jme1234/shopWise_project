package com.example.demo.domain.services;

import com.example.demo.domain.model.Client;
import com.example.demo.infrastructure.repository.ClientRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    public ClientRespository clientRespository;


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

}

