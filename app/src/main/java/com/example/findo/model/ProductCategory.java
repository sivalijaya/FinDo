package com.example.findo.model;

import java.util.ArrayList;

public class ProductCategory {
    private int id;
    private String name;
    private ArrayList<Product> mproduct;

    public ProductCategory() {
    }

    public ProductCategory(int id, String name, ArrayList<Product> product) {
        this.id = id;
        this.name = name;
        this.mproduct = product;
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

    public ArrayList<Product> getMproduct() {
        return mproduct;
    }

    public void setMproduct(ArrayList<Product> mproduct) {
        this.mproduct = mproduct;
    }
}
