package com.example.demo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    public Shop() {
    }

    public Shop(String nom, String city) {
        this.nom = nom;
        this.city = city;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
