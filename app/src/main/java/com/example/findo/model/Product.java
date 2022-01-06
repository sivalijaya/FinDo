package com.example.findo.model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Product {
    private Integer id;
    private String name;
    private Integer sold;
    private Long price;
    private Integer stock;
    private String description;
    private ArrayList<String> photo;
    private String gender;
    private String brand;
    private String shippingFrom;

    public Product() {
    }

    public Product(DataSnapshot productSnapshot) {
        this.id = Integer.parseInt(productSnapshot.getKey());
        this.name = productSnapshot.child("name").getValue().toString();
        this.sold = Integer.parseInt(productSnapshot.child("sold").getValue().toString());
        this.price = Long.parseLong(productSnapshot.child("price").getValue().toString());
        ArrayList<String> productImages = new ArrayList<>();
        for (DataSnapshot productImageSnapshot : productSnapshot.child("images").getChildren()) {
            productImages.add(productImageSnapshot.getValue().toString());
        }
        this.photo = productImages;
        this.gender = productSnapshot.child("gender").getValue().toString();
        this.brand = productSnapshot.child("brand").getValue().toString();
        this.shippingFrom = productSnapshot.child("shippingFrom").getValue().toString();
    }

    public Product(Integer id, String name, Integer sold, Long price, ArrayList<String> photo) {
        this.id = id;
        this.name = name;
        this.sold = sold;
        this.price = price;
        this.photo = photo;
    }

    public Product(Integer id, String name, Integer sold, Long price, Integer stock, String description, ArrayList<String> photo, String gender) {
        this.id = id;
        this.name = name;
        this.sold = sold;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.photo = photo;
        this.gender = gender;
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

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getShippingFrom() {
        return shippingFrom;
    }

    public void setShippingFrom(String shippingFrom) {
        this.shippingFrom = shippingFrom;
    }
}
