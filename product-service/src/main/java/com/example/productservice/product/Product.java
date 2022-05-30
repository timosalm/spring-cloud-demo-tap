package com.example.productservice.product;

public class Product {

    private Long id;
    private String name;

    private Product(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Product create(Long id, String name) {
        return new Product(id, name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
