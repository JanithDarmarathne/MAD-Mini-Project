package com.example.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dashboard.Models.ProductUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddingProducts extends AppCompatActivity {


    public String Name = "Name";
    public  String Made = "Made";
    public  String Price = "Price";
    public String ImageUrl = "ImageUrl";
    public String Push = "pushKey";
    private TextView name,madeIn,price;
    private ImageView productImage;
    private Button btnCart, btnBuy;
    private FirebaseAuth mAuth;
    private  String userId;
    private DatabaseReference myRef;
    private ProgressDialog progressDialog;
    public String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_products);

        Intent i = getIntent();

        final String productName = i.getStringExtra(Name);
        final String productMade = i.getStringExtra(Made);
        final String productPrice = i.getStringExtra(Price);
        final String proudctImageUrl = i.getStringExtra(ImageUrl);
        final String pushKey = i.getStringExtra(Push);
        mAuth = FirebaseAuth.getInstance();



        btnCart = findViewById(R.id.button20);
        btnBuy = findViewById(R.id.button21);
        name  = findViewById(R.id.category19);
        madeIn = findViewById(R.id.category20);
        price = findViewById(R.id.category21);
        productImage = findViewById(R.id.imageView10);

        name.setText(productName);
        madeIn.setText(productMade);
        price.setText(productPrice);
        Glide.with(this).load(proudctImageUrl).into(productImage);


        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = myRef.getKey();
                ProductUser product = new ProductUser(productName,productPrice,productMade,key,proudctImageUrl,0);
                myRef.setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {


                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                openPayment();

            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                key = myRef.getKey();
                ProductUser product = new ProductUser(productName,productPrice,productMade,key,proudctImageUrl,0);
                myRef.setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddingProducts.this,"Successfully Added to cart", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AddingProducts.this,"Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });


    }

    private void openPayment() {
        Intent intent = new Intent(this,MainActivityPayment.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
            myRef = FirebaseDatabase.getInstance().getReference("Cart").push();
          //  Toast.makeText(AddingProducts.this,userId.toString(),Toast.LENGTH_SHORT).show();
        }else{
          //  Toast.makeText(AddingProducts.this,"No USER",Toast.LENGTH_SHORT).show();
        }
    }
}