package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.RendezVous;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RendezVousRespository extends JpaRepository<RendezVous, Integer> {

    List<RendezVous> findByServiceShopId(Integer shopId);   // RendezVous -> Service -> Shop

    @NullMarked
    Optional<RendezVous> findById(Integer id);

}
