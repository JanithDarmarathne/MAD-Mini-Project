package com.example.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivityPayment extends AppCompatActivity implements ProductPayAdapter.onItemClicked  {

    FirebaseAuth mAuth;
    DatabaseReference myRef;
    ValueEventListener valueEventListener;
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private ArrayList<ProductUser> list;
    private LinearLayoutManager linearLayoutManager;
    private ProductPayAdapter productPayAdapter;
    int sum = 0;
    EditText etSum;
    Button btnBack,btnProcced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpayment);
        mAuth = FirebaseAuth.getInstance();
        etSum = findViewById(R.id.editTextNumber);
        btnBack = findViewById(R.id.button);
        btnProcced = findViewById(R.id.button2);
        recyclerView = findViewById(R.id.recyclerView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Your Cart");
        progressDialog.setMessage("Loading...");

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        btnProcced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Confirmation.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.signInAnonymously()
                .addOnCompleteListener(MainActivityPayment.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            myRef = FirebaseDatabase.getInstance().getReference("Cart");
                            list = new ArrayList<>();

                            progressDialog.show();
                            InitListner();
                            myRef.addValueEventListener(valueEventListener);
                          //  Toast.makeText(getApplicationContext(),"Successfully logged into your account!",Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getApplicationContext(),"Please Try Again!",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void InitListner(){
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    ProductUser model = data.getValue(ProductUser.class);
                    list.add(model);
                    sum += model.noOfItems;


                }

                etSum.setText(sum+"");

                productPayAdapter = new ProductPayAdapter(list,getApplicationContext());
                recyclerView.setAdapter(productPayAdapter);
                productPayAdapter.notifyDataSetChanged();
                productPayAdapter.SetOnItemClickListener(MainActivityPayment.this);
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

    }
}