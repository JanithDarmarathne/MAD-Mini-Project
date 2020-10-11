package com.example.dashboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard.Models.ProductUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart extends AppCompatActivity implements CartAdapter.onItemClicked {

    private RecyclerView recyclerView;
    private ArrayList<ProductUser> list;
    private CartAdapter productAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ValueEventListener valueEventListener;
    private LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;
    public String userId;
    private Button btnPlus,btnMin;
    private EditText etNoofItems;
    int i =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.cartRecycler);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Your Cart");
        progressDialog.setMessage("Loading...");

        Intent i = getIntent();
        userId = i.getStringExtra("userID");
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("Cart");

        list = new ArrayList<>();
        progressDialog.show();
        InitListner();
        mDatabase.addValueEventListener(valueEventListener);
    }

    private void InitListner(){
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    ProductUser model = data.getValue(ProductUser.class);
                    list.add(model);


                }

                productAdapter = new CartAdapter(list,getApplicationContext());
                recyclerView.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
                productAdapter.SetOnItemClickListener(Cart.this);
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

        ProductUser model = list.get(index);
        myDialog(model);
    }

    private void myDialog(final ProductUser product){
        final AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        myDialog.setTitle("No of Items");


        LayoutInflater inflater = LayoutInflater.from(this);
        View myCustomlayout = inflater.inflate(R.layout.customdialog,null);

        btnMin = myCustomlayout.findViewById(R.id.minbtn);
        //productName.setHint("Comment");

        btnPlus = myCustomlayout.findViewById(R.id.plusbtn);
      //  productCategory.setVisibility(View.GONE);
        etNoofItems = myCustomlayout.findViewById(R.id.editNum);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++i;
                etNoofItems.setText(i+"");
            }
        });

        etNoofItems.setText(product.noOfItems+"");

        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --i;
                etNoofItems.setText(i+"");
            }
        });

        myDialog.setView(myCustomlayout);

        myDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(),i+"",Toast.LENGTH_SHORT).show();

                product.noOfItems = i;
                mDatabase.child(product.pushKey).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Saved No of items",Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      //  Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }

        });

        myDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
           // userId = user.getUid();
        }else{

        }
    }
}