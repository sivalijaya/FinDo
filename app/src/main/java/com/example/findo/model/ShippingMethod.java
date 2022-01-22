package com.example.findo.model;

import com.google.firebase.database.DataSnapshot;

public class ShippingMethod {
    private Integer id;
    private String name;
    private String image;
    private Integer price;
    private String estimation;

    public ShippingMethod(DataSnapshot shippingMethodSnapshot) {
        this.id = Integer.parseInt(shippingMethodSnapshot.getKey());
        this.name = shippingMethodSnapshot.child("name").getValue().toString();
        this.image = shippingMethodSnapshot.child("image").getValue().toString();
        this.price = Integer.parseInt(shippingMethodSnapshot.child("price").getValue().toString());
        this.estimation = shippingMethodSnapshot.child("estimation").getValue().toString();
    }

    public ShippingMethod(Integer id, String name, String image, Integer price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public ShippingMethod(Integer id, String name, String image, Integer price, String estimation) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.estimation = estimation;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getEstimation() {
        return estimation;
    }

    public void setEstimation(String estimation) {
        this.estimation = estimation;
    }
}
