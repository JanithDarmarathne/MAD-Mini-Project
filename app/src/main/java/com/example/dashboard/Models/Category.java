package com.example.dashboard.Models;

import android.net.Uri;

public class Category {

    public String CategoryName;
    public String FilePath;
    public String pushKey;

    public Category(){

    }

    public Category(String cat,String File,String key){
        CategoryName = cat;
        FilePath = File;
        pushKey = key;
    }
}
