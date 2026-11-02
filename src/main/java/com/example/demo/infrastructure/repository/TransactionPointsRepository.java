package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.TransactionPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionPointsRepository extends JpaRepository<TransactionPoints, Integer> {

    List<TransactionPoints> findByClientIdOrderByIdDesc(Integer clientId);   // la plus récente en premier

}
