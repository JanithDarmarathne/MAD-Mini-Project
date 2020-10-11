package com.example.dashboard.Models;

public class Product {

    public int Id;
    public String Name;
    public String productPrice;
    public String productMade;
    public String pushKey;
    public String ImageUrl;

    public Product(){

    }

    public Product(String name, String productprice, String productmade, String key, String imageUrl){

        Name = name;
        productPrice = productprice;
        productMade = productmade;
        ImageUrl = imageUrl;
        pushKey = key;

    }
}
