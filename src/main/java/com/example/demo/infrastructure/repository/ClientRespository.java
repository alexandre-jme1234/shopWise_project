package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.Client;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRespository extends JpaRepository<Client, Integer> {

    @Transactional
    default Client updateOrInsert(Client entity) {
        return save(entity);
    }
}
