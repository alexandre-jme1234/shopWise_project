package com.example.demo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Facturation")
public class Facturation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    public Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime date_facturation;

    public String nom;

    public BigDecimal montant;

    public Facturation() {
    }

    public Facturation(Client client, Service service, LocalDateTime date_facturation, String nom, BigDecimal montant) {
        this.client = client;
        this.service = service;
        this.date_facturation = date_facturation;
        this.nom = nom;
        this.montant = montant;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public LocalDateTime getDate_facturation() {
        return date_facturation;
    }

    public void setDate_facturation(LocalDateTime date_facturation) {
        this.date_facturation = date_facturation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}
