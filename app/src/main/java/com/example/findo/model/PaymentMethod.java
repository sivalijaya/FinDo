package com.example.findo.model;

import com.google.firebase.database.DataSnapshot;

public class PaymentMethod {
    private int id;
    private String name;
    private String image;

    public PaymentMethod(DataSnapshot paymentMethodSnapshot) {
        this.id = Integer.parseInt(paymentMethodSnapshot.child("id").getValue().toString());
        this.name = paymentMethodSnapshot.child("name").getValue().toString();
        this.image = paymentMethodSnapshot.child("image").getValue().toString();
    }

    public PaymentMethod(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
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
}
