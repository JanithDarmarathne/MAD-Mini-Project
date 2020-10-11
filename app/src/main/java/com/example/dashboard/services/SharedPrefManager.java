package com.example.dashboard.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dashboard.Models.User;

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private static Context ctx;
    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_Address = "userAddress";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_IMAGE = "userImage";


    private SharedPrefManager(Context context) {
        ctx = context;


    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public boolean userLogin(String name, String email, String Address){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_EMAIL,email);
        editor.putString(KEY_USERNAME,name);
        editor.putString(KEY_USER_Address,Address);
     //   editor.putString(KEY_IMAGE,bitmap.toString());

        editor.apply();

        return true;

    }

    public User userDetails(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME,null) != null){
             User user = new User(sharedPreferences.getString(KEY_USERNAME,null) ,sharedPreferences.getString(KEY_USER_EMAIL,null) ,sharedPreferences.getString(KEY_USER_Address,null) );
             return  user;

        }
        return  null;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME,null) != null){
            return true;
        }

        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }



}
