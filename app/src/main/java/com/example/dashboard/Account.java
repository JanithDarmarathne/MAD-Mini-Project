package com.example.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dashboard.Models.User;
import com.example.dashboard.services.SharedPrefManager;
import com.example.dashboard.services.ShopDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Account extends AppCompatActivity {

    User user;
    public TextView userEmail,txtCategory,txtProduct,txtComments,txtAccounts,lgOut;
    public ImageView imageView;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        userEmail = findViewById(R.id.textView14);
        txtCategory = findViewById(R.id.textView15);
        txtProduct = findViewById(R.id.textView16);
        txtComments = findViewById(R.id.textView17);
        txtAccounts = findViewById(R.id.textView18);
        imageView = findViewById(R.id.imageView3);


        mAuth = FirebaseAuth.getInstance();

        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                startActivity(intent);
            }
        });

        txtProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProduct.class);
                startActivity(intent);
            }
        });

        txtAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Money.class);
                startActivity(intent);
            }
        });

        txtComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Comment.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user1 = mAuth.getCurrentUser();

        if(!SharedPrefManager.getInstance(getApplicationContext())
                .isLoggedIn() && user1 == null){

            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
            finish();
        }else {
            user = SharedPrefManager.getInstance(getApplicationContext()).userDetails();
            String name = user.Name;
            userEmail.setText(name);
            userEmail.setPadding(50,0,0,0);

            ShopDB db = new ShopDB(getApplicationContext());
            db.open();
            Bitmap bit = db.getUserImage(user.Email,user.Name);
           // imageView.setImageBitmap(bit);
            Glide.with(this).load(bit).override(400,400).circleCrop().into(imageView);
            imageView.setPadding(0,0,40,0);

        }
    }

    public void logout(View view){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPrefManager.getInstance(getApplicationContext()).logout();

                        if(!SharedPrefManager.getInstance(getApplicationContext())
                                .isLoggedIn()){
                            mAuth.signOut();
                            Intent intent = new Intent(getApplicationContext(), SignIn.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}