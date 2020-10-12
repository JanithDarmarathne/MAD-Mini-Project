package com.example.dashboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard.Models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductsList extends AppCompatActivity implements ProductAdapter.onItemClicked {

    private RecyclerView recyclerView;
    private ArrayList<Product> list;
    private ProductAdapter productAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ValueEventListener valueEventListener;
    private LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;
    private Button btnCart;
    public String userId;
    ImageView imageView;

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productslistt);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView2);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Products List");
        progressDialog.setMessage("Loading...");

        btnCart = findViewById(R.id.button);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductsList.this,Cart.class);
                i.putExtra("userID",userId.toString());
                startActivity(i);
               // Toast.makeText(ProductsList.this,userId.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference("product");
        list = new ArrayList<>();
        progressDialog.show();
        InitListner();
        mDatabase.addValueEventListener(valueEventListener);

    }

    private void open() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //   deleteProduct(id);
                        mAuth.getInstance()
                                .signOut();
                        startActivity(new Intent(ProductsList.this, LoginUser.class));
                        finish();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //  alertDialogBuilder.
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void InitListner(){
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Product model = data.getValue(Product.class);
                    list.add(model);


                }

                productAdapter = new ProductAdapter(list,getApplicationContext());
                recyclerView.setAdapter(productAdapter);
                productAdapter.SetOnItemClickListener(ProductsList.this);
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }

        };
    }

    @Override
    public void OnItemClick(int index) {
        Intent i = new Intent(getApplicationContext(),AddingProducts.class);
        Product model = list.get(index);
       // i.putExtra(RENDER_CONTENT,model.render);
        // i.putExtra(title,model.title);
        i.putExtra("Name",model.Name);
        i.putExtra("Made",model.productMade);
        i.putExtra("Price",model.productPrice);
        i.putExtra("ImageUrl",model.ImageUrl);
        i.putExtra("pushKey",model.pushKey);
        startActivity(i);
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }else{
            Toast.makeText(this,"No USER",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),loginWelcomePage.class);
            startActivity(i);
            finish();
        }
    }

 */


}