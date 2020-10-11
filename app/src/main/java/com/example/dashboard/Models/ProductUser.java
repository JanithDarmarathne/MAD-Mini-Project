package com.example.dashboard.Models;

public class ProductUser {
    public int Id;
    public String Name;
    public String productPrice;
    public String productMade;
    public String pushKey;
    public String ImageUrl;
    public int noOfItems;

    public ProductUser(){
    }

    public ProductUser(String name, String productprice, String productmade, String key, String imageUrl, int noofItem){

        Name = name;
        productPrice = productprice;
        productMade = productmade;
        ImageUrl = imageUrl;
        pushKey = key;
        noOfItems = noofItem;
    }
}
