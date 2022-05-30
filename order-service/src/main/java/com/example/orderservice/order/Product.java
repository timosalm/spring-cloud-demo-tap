package com.example.orderservice.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
class Product implements Serializable { // Serializable required for Redis cache

    private static final long serialVersionUID = 2364007368011758242L;

    private Long id;

    Product() {
    }

    Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + '}';
    }
}
