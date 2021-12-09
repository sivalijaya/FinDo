package com.example.findo.model;

public class Product {
    private int id;
    private int productCategoryId;
    private String name;
    private int sold;
    private int price;
    private int stock;
    private String brand;
    private String shippingFrom;
    private String description;
    private String[] photo;

    public Product() {
    }

    public Product(int id, int productCategoryId, String name, int sold, int price, int stock, String brand, String shippingFrom, String description, String[] photo) {
        this.id = id;
        this.productCategoryId = productCategoryId;
        this.name = name;
        this.sold = sold;
        this.price = price;
        this.stock = stock;
        this.brand = brand;
        this.shippingFrom = shippingFrom;
        this.description = description;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getPhoto() {
        return photo;
    }

    public void setPhoto(String[] photo) {
        this.photo = photo;
    }


}
