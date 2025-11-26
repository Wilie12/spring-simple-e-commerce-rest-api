package com.nn.spring_simple_e_commerce_rest_api.cart.domain;

import com.nn.spring_simple_e_commerce_rest_api.product.domain.Product;
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
    private Map<Product, Integer> products = new HashMap<>();

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

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void addProduct(Product product, int quantity) {
        products.put(product, quantity);
    }
}
