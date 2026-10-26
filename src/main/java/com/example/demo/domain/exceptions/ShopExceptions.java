package com.example.demo.domain.exceptions;

import com.example.demo.domain.model.Shop;

public class ShopExceptions extends RuntimeException  {
    public ShopExceptions(Integer id) {
        super("Shop " + id + " introuvable");
    }
};
