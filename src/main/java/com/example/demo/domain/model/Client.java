package com.example.demo.domain.model;


import jakarta.persistence.*;

@Entity
@Table(name="Client")
@PrimaryKeyJoinColumn(name = "user_id")
public class Client extends User {

    @Column(name = "solde_points")
    public Integer solde_points;

    public Integer getSolde_points() {
        return solde_points;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    public Shop shop;

    public void setSolde_points(Integer solde_points) {
        this.solde_points = solde_points;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword_hash(password);
    }

    @Override
    public void setPhone(String phone) {
        super.setPhone(phone);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setId(Integer id) {
        super.setId(id);
    }

    public Client() {
        super();
    }

    public void crediter(int points) {
        // quelle vérification avant d'ajouter ?
        this.solde_points += points;
    }

    public void debiter(int points) {
        // quelles vérifications avant de retirer ?
        this.solde_points -= points;}

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public String getPassword() {
        return super.getPassword_hash();
    }

    @Override
    public String getPhone() {
        return super.getPhone();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return "Clients{" +
                "solde_points=" + solde_points +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
