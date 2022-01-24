package com.example.findo.model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Transaction {
    private Boolean gift_wrapping;
    private String order_id;
    private PaymentMethod payment_method;
    private ProductCategory product_category;
    private Product product;
    private Recipient recipient;
    private ShippingMethod shipping_method;
    private Integer status;
    private String time_issued;
    private String virtual_account;
    private Integer quantity;

    public Transaction(DataSnapshot transactionSnapshot) {
        this.gift_wrapping = Boolean.parseBoolean(transactionSnapshot.child("gift_wrapping").getValue().toString());
        this.order_id = transactionSnapshot.child("gift_wrapping").getValue().toString();

        ArrayList<String> product_image = new ArrayList<>();
        Integer product_id = Integer.parseInt(transactionSnapshot.child("product").child("id").getValue().toString());
        for (DataSnapshot productImageSnapshot : transactionSnapshot.child("product").child("images").getChildren()) {
            product_image.add(productImageSnapshot.getValue().toString());
        }
        String product_name = transactionSnapshot.child("product").child("name").getValue().toString();
        Integer product_price = Integer.parseInt(transactionSnapshot.child("product").child("price").getValue().toString());
        this.product = new Product(product_id, product_name, product_price, product_image);

        Integer paymentMethod_id = Integer.parseInt(transactionSnapshot.child("payment_method").child("id").getValue().toString());
        String paymentMethod_name = transactionSnapshot.child("payment_method").child("name").getValue().toString();
        String paymentMethod_image = transactionSnapshot.child("payment_method").child("image").getValue().toString();
        this.payment_method = new PaymentMethod(paymentMethod_id, paymentMethod_name, paymentMethod_image);


        Integer productCategory_id = Integer.parseInt(transactionSnapshot.child("product_category").child("id").getValue().toString());
        String productCategory_name = transactionSnapshot.child("product_category").child("name").getValue().toString();
        this.product_category = new ProductCategory(productCategory_id, productCategory_name);

        String recipientName = transactionSnapshot.child("recipient").child("name").getValue().toString();
        String recipientEmail = transactionSnapshot.child("recipient").child("email").getValue().toString();
        String recipientPhoneNumber = transactionSnapshot.child("recipient").child("phone_number").getValue().toString();
        String recipientAddress = transactionSnapshot.child("recipient").child("address").getValue().toString();
        this.recipient = new Recipient(recipientAddress, recipientEmail, recipientName, recipientPhoneNumber);


        Integer shippingMethod_id = Integer.parseInt(transactionSnapshot.child("shipping_method").child("id").getValue().toString());
        String shippingMethod_name = transactionSnapshot.child("shipping_method").child("name").getValue().toString();
        String shippingMethod_image = transactionSnapshot.child("shipping_method").child("image").getValue().toString();
        Integer shippingMethod_price = Integer.parseInt(transactionSnapshot.child("shipping_method").child("price").getValue().toString());
        this.shipping_method = new ShippingMethod(shippingMethod_id, shippingMethod_name, shippingMethod_image, shippingMethod_price);


        this.status = Integer.parseInt(transactionSnapshot.child("status").getValue().toString());
        this.time_issued = transactionSnapshot.child("time_issued").getValue().toString();
        this.virtual_account = transactionSnapshot.child("virtual_account").getValue().toString();
        this.quantity = Integer.parseInt(transactionSnapshot.child("quantity").getValue().toString());
    }

    public Transaction(Boolean gift_wrapping, String order_id, ProductCategory product_category, Product product, Recipient recipient, ShippingMethod shipping_method, Integer status, String time_issued, Integer quantity) {
        this.gift_wrapping = gift_wrapping;
        this.order_id = order_id;
        this.product_category = product_category;
        this.product = product;
        this.recipient = recipient;
        this.shipping_method = shipping_method;
        this.status = status;
        this.time_issued = time_issued;
        this.quantity = quantity;
    }

    public Boolean getGift_wrapping() {
        return gift_wrapping;
    }

    public void setGift_wrapping(Boolean gift_wrapping) {
        this.gift_wrapping = gift_wrapping;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public PaymentMethod getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(PaymentMethod payment_method) {
        this.payment_method = payment_method;
    }

    public ProductCategory getProduct_category() {
        return product_category;
    }

    public void setProduct_category(ProductCategory product_category) {
        this.product_category = product_category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public ShippingMethod getShipping_method() {
        return shipping_method;
    }

    public void setShipping_method(ShippingMethod shipping_method) {
        this.shipping_method = shipping_method;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTime_issued() {
        return time_issued;
    }

    public void setTime_issued(String time_issued) {
        this.time_issued = time_issued;
    }

    public String getVirtual_account() {
        return virtual_account;
    }

    public void setVirtual_account(String virtual_account) {
        this.virtual_account = virtual_account;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
