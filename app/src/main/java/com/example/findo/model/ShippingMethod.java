package com.example.findo.model;

import com.google.firebase.database.DataSnapshot;

public class ShippingMethod {
    private Integer id;
    private String name;
    private String image;
    private Integer price;

    public ShippingMethod(DataSnapshot shippingMethodSnapshot) {
        this.id = Integer.parseInt(shippingMethodSnapshot.child("id").getValue().toString());
        this.name = shippingMethodSnapshot.child("name").getValue().toString();
        this.image = shippingMethodSnapshot.child("image").getValue().toString();
    }

    public ShippingMethod(Integer id, String name, String image, Integer price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
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
}
