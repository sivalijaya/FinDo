package com.example.findo.model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Product {
    private String id;
    private String name;
    private String sold;
    private String price;
    private String stock;
    private String description;
    private ArrayList<String> photo;
    private String gender;

    public Product() {
    }

    public Product(DataSnapshot productSnapshot) {
        this.id = productSnapshot.getKey();
        this.name = productSnapshot.child("name").getValue().toString();
        this.sold = productSnapshot.child("sold").getValue().toString();
        this.price = productSnapshot.child("price").getValue().toString();
        ArrayList<String> productImages = new ArrayList<>();
        for (DataSnapshot productImageSnapshot : productSnapshot.child("images").getChildren()) {
            productImages.add(productImageSnapshot.getValue().toString());
        }
        this.photo = productImages;
    }

    public Product(String id, String name, String sold, String price, ArrayList<String> photo) {
        this.id = id;
        this.name = name;
        this.sold = sold;
        this.price = price;
        this.photo = photo;
    }

    public Product(String id, String name, String sold, String price, String stock, String description, ArrayList<String> photo, String gender) {
        this.id = id;
        this.name = name;
        this.sold = sold;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.photo = photo;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<String> photo) {
        this.photo = photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
