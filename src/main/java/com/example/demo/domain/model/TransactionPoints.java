package com.example.demo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TransactionPoints")
public class TransactionPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rendez_vous_id")
    private RendezVous rendez_vous;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_transaction")
    public LocalDateTime date_heure;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private Type type = Type.GAIN;

    public Integer points;

    public TransactionPoints() {
    }

    public TransactionPoints(Client client, RendezVous rendez_vous, LocalDateTime date_heure, Type type, Integer points) {
        this.client = client;
        this.rendez_vous = rendez_vous;
        this.date_heure = date_heure;
        this.type = type;
        this.points = points;
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

    public RendezVous getRendez_vous() {
        return rendez_vous;
    }

    public void setRendez_vous(RendezVous rendez_vous) {
        this.rendez_vous = rendez_vous;
    }

    public LocalDateTime getDate_heure() {
        return date_heure;
    }

    public void setDate_heure(LocalDateTime date_heure) {
        this.date_heure = date_heure;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
