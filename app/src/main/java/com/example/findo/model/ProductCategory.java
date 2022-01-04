package com.example.findo.model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class ProductCategory {
    private Integer id;
    private String name;
    private ArrayList<Product> product;

    public ProductCategory() {
    }

    public ProductCategory(DataSnapshot categorySnapshot) {
        this.id = Integer.parseInt(categorySnapshot.getKey());
        this.name = categorySnapshot.child("name").getValue().toString();
        ArrayList<Product> productList = new ArrayList<>();
        for (DataSnapshot productSnapshot : categorySnapshot.child("product").getChildren()) {
            productList.add(new Product(productSnapshot));
        }
        this.product = productList;
    }

    public ProductCategory(Integer id, String name, ArrayList<Product> product) {
        this.id = id;
        this.name = name;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }
}
