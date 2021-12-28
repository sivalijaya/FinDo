package com.example.findo.model;

public class ProductCategory {
    private int id;
    private String name;
    private Product product;

    public ProductCategory() {
    }

    public ProductCategory(int id, String name, Product product) {
        this.id = id;
        this.name = name;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
