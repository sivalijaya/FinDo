package com.example.findo.model;

import com.google.firebase.database.DataSnapshot;

public class PaymentMethod {
    private String name;
    private String image;

    public PaymentMethod(DataSnapshot paymentMethodSnapshot) {
        this.name = paymentMethodSnapshot.child("name").getValue().toString();
        this.image = paymentMethodSnapshot.child("image").getValue().toString();
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
