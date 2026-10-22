package com.example.demo.domain.model;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    public Shop shop;

    public String produit;

    public BigDecimal prix;

    public Integer points;

    public Service() {
    }

    public Service(Shop shop, String produit, BigDecimal prix, Integer points) {
        this.shop = shop;
        this.produit = produit;
        this.prix = prix;
        this.points = points;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
