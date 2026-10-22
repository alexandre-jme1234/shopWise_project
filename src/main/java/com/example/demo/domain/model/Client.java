package com.example.demo.domain.model;


import jakarta.persistence.Entity;

@Entity
public class Client extends User {
    public Integer solde_points;

    public Integer getSolde_points() {
        return solde_points;
    }

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
        super.setPassword(password);
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

    public Clients() {
        super();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
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
