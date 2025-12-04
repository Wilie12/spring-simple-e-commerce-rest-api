package com.nn.spring_simple_e_commerce_rest_api.cart.domain;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity(name = "carts")
public class Cart {
    @Id
    @GeneratedValue
    private int id;
    private String username;
    @ElementCollection
    private Map<Long, Integer> products = new HashMap<>();

    protected Cart() {}

    public Cart(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void addProduct(Long productId, int quantity) {
        products.put(productId, quantity);
    }

    public void clearProducts() {
        products.clear();
    }
}
