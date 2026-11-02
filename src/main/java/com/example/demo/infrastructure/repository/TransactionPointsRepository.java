package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.TransactionPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionPointsRepository extends JpaRepository<TransactionPoints, Integer> {

}
