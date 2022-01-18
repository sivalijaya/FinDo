package com.example.findo.model;

public class Recipient {
    private String address;
    private String email;
    private String name;
    private String phone_number;

    public Recipient(String address, String email, String name, String phone_number) {
        this.address = address;
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
