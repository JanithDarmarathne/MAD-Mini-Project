package com.example.dashboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dashboard.Models.ProductUser;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddProduct extends AppCompatActivity {
    private Button btnProduct;
    private TextInputLayout productName,productCategory;
    ArrayList<ProductUser> list;
    private TableLayout stk;
    private DatabaseReference mDatabase;
    ValueEventListener valueEventListener;
    public ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        btnProduct = findViewById(R.id.button3);
        progressDialog = new ProgressDialog(this);

        stk = findViewById(R.id.tableLayout);

        list = new ArrayList<>();
        list.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("product");
        InitListner();
        mDatabase.addValueEventListener(valueEventListener);

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddProducts.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddProduct.this,Account.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(i);
    }

    public void open(final String id){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete Product");
        alertDialogBuilder.setMessage("Are you sure?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                mDatabase.child(id).removeValue();
                                Toast.makeText(getApplicationContext(),"Successfully deleted the item",Toast.LENGTH_SHORT).show();
                                //startActivity(getActivity().getIntent());
                                AddProduct.super.onBackPressed();
                                finish();
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




    private void InitListner(){
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        list.clear();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    ProductUser model = data.getValue(ProductUser.class);
                    list.add(model);

                }

                for (int i = 0; i < list.size(); i++) {
                    final TableRow tbrow = new TableRow(getApplicationContext());

                    TextView t1v = new TextView(getApplicationContext());
                    t1v.setText(list.get(i).Name.toString());
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.CENTER);
                    t1v.setTextSize(18);
                    t1v.setPadding(5,20,5,5);
                    tbrow.addView(t1v);

                    ImageView t2v = new ImageView(getApplicationContext());
                    t2v.setPadding(5,20,5,5);
                    Glide.with(getApplicationContext())
                            .load(list.get(i).ImageUrl.toString()).into(t2v);
                    t2v.setMaxWidth(100);
                    t2v.setMaxHeight(100);
                    tbrow.addView(t2v);

                    TextView t3v = new TextView(getApplicationContext());
                    t3v.setText("Delete");
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.CENTER);
                    t3v.setPadding(50,20,0,5);
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(getApplicationContext());
                    t4v.setText("Edit");
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.CENTER);
                    t4v.setPadding(50,20,0,5);
                    tbrow.addView(t4v);
                    tbrow.setTag(list.get(i).pushKey);


                    t3v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String x = tbrow.getTag().toString();

                            open(x);

                        }
                    });

                    t4v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String x = tbrow.getTag().toString();
                            Intent i = new Intent(AddProduct.this,EditProduct.class);
                            i.putExtra("key",x);
                            startActivity(i);
                          //  Toast.makeText(getApplicationContext(),x,Toast.LENGTH_SHORT).show();
//                            getFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.editcat, new EditCategoryFragment())
//                                    .commit();
                        }
                    });

                    stk.addView(tbrow);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
    }

}