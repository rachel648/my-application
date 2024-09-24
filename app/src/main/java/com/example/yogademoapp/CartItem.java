package com.example.yogademoapp;

public class CartItem {
    private String product;
    private double price;
    private String otype;

    public CartItem() {
        // Default constructor required for Firebase
    }

    public CartItem(String product, double price, String otype) {
        this.product = product;
        this.price = price;
        this.otype = otype;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }
}


// jioo