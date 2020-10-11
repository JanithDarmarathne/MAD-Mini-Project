package com.example.eshoppingmanagementsytemuser.Models;

public class Product {

    public int Id;
    public String Name;
    public String productPrice;
    public String productMade;
    public String pushKey;
    public String ImageUrl;
    public int noOfItems;

    public Product(){

    }

    public Product(String name, String productprice, String productmade, String key, String imageUrl, int noofItem){

        Name = name;
        productPrice = productprice;
        productMade = productmade;
        ImageUrl = imageUrl;
        pushKey = key;
        noOfItems = noofItem;

    }
}
