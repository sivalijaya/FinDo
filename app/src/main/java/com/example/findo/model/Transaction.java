package com.example.findo.model;

import com.google.firebase.database.DataSnapshot;

public class Transaction {
    private Boolean gift_wrapping;
    private String order_id;
    private Integer payment_method_id;
    private Integer product_category_id;
    private Integer product_id;
    private Recipient recipient;
    private Integer shipping_method_id;
    private Integer status;
    private String time_issued;
    private String virtual_account;
    private Integer quantity;

    public Transaction(DataSnapshot transactionSnapshot) {
        this.gift_wrapping = Boolean.parseBoolean(transactionSnapshot.child("gift_wrapping").getValue().toString());
        this.order_id = transactionSnapshot.child("gift_wrapping").getValue().toString();
        this.payment_method_id = Integer.parseInt(transactionSnapshot.child("payment_method_id").getValue().toString());
        this.product_category_id = Integer.parseInt(transactionSnapshot.child("product_category_id").getValue().toString());
        this.product_id = Integer.parseInt(transactionSnapshot.child("product_id").getValue().toString());
        String recipientName = transactionSnapshot.child("recipient").child("name").getValue().toString();
        String recipientEmail = transactionSnapshot.child("recipient").child("email").getValue().toString();
        String recipientPhoneNumber = transactionSnapshot.child("recipient").child("phone_number").getValue().toString();
        String recipientAddress = transactionSnapshot.child("recipient").child("address").getValue().toString();
        this.recipient = new Recipient(recipientAddress, recipientEmail, recipientName, recipientPhoneNumber);
        this.shipping_method_id = Integer.parseInt(transactionSnapshot.child("shipping_method_id").getValue().toString());
        this.status = Integer.parseInt(transactionSnapshot.child("status").getValue().toString());
        this.time_issued = transactionSnapshot.child("time_issued").getValue().toString();
        this.virtual_account = transactionSnapshot.child("virtual_account").getValue().toString();
        this.quantity = Integer.parseInt(transactionSnapshot.child("quantity").getValue().toString());
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

    public Integer getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(Integer payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public Integer getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(Integer product_category_id) {
        this.product_category_id = product_category_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public Integer getShipping_method_id() {
        return shipping_method_id;
    }

    public void setShipping_method_id(Integer shipping_method_id) {
        this.shipping_method_id = shipping_method_id;
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
