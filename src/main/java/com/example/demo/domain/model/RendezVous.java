package com.example.demo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "RendezVous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    public Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    public Service service;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "statut_code", nullable = false, length = 20)
    private Statut statut = Statut.PLANIFIE;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_heure")
    public LocalDateTime date_heure;

    public RendezVous() {
    }

    public RendezVous(Client client, Service service, LocalDateTime date_heure) {
        this.client = client;
        this.service = service;
        this.date_heure = date_heure;
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

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public LocalDateTime getDate_heure() {
        return date_heure;
    }

    public void setDate_heure(LocalDateTime date_heure) {
        this.date_heure = date_heure;
    }
}
