package com.example.demo.application.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;

public record ClientCreateRequest(
        @NotBlank @Size(max = 100)
        String name,

        @NotBlank @Email @Size(max = 150)       // 150 = VARCHAR(150) de la colonne email
        String email,

        @NotBlank @Size(min = 8, max = 72)      // 8 minimum ; 72 = limite de BCrypt
        String password,                        // minuscule : c'est aussi la clé JSON

        @Size(max = 20)                         // facultatif, mais limité à VARCHAR(20)
        String phone
    )
{}
