package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.User;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.service.registry.HttpServiceGroup;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRespository extends JpaRepository<Client, Integer> {

    List<Client> findByShopId(Integer shopId);

    @NullMarked
    Optional<Client> findById(Integer id);

    @Transactional
    default Client updateOrInsert(Client entity) {
        return save(entity);
    }



}
