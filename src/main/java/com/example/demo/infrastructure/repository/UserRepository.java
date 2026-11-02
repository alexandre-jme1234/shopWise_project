package com.example.demo.infrastructure.repository;


import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.User;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Integer> {
    boolean existsByEmail(String email);

    @NullMarked
    Optional<User> findById(Integer id);
};

