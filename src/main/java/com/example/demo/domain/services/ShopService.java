package com.example.demo.domain.services;

import com.example.demo.domain.exceptions.ShopExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.Shop;
import com.example.demo.infrastructure.repository.ClientRespository;
import com.example.demo.infrastructure.repository.ShopRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ClientRespository clientRepository;

    public List<Client> getClientsByShop(Integer id){
      if(!shopRepository.existsById(id)) {
          throw new ShopExceptions(id);
      }
      return clientRepository.findByShopId(id);
    };

    public Optional<Shop> getShopById(Integer id){
        if(!shopRepository.existsById(id)) {
            throw new ShopExceptions(id);
        }
        return shopRepository.findById(id);
    };

    public boolean siShopExisted(Integer id) {
        if(!shopRepository.existsById(id)) {
            throw new ShopExceptions(id);
        } return true;
    }
}
