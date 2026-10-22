package com.example.demo.domain.model;

import jakarta.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class StoreManager extends User {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    public StoreManager() {
        super();
    }

    public StoreManager(Shop shop) {
        super();
        this.shop = shop;
    }

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
    public void setPassword(String password) {
        super.setPassword_hash(password);
    }
}
