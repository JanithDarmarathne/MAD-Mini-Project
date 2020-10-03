package com.example.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Category extends AppCompatActivity {

    private ArrayList<com.example.seller.models.Category> list;
    private TableLayout tableLayout;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ValueEventListener valueEventListener;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        progressDialog = new ProgressDialog(this);
        tableLayout = findViewById(R.id.tableLayout);

        mDatabase = FirebaseDatabase.getInstance().getReference("category");
        InitListner();
        mDatabase.addValueEventListener(valueEventListener);
        list = new ArrayList<>();
    }


    private void InitListner(){

        progressDialog.setTitle("Loading...");
        progressDialog.show();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    com.example.seller.models.Category model = data.getValue(com.example.seller.models.Category.class);
                    list.add(model);

                }

                for (int i = 0; i < list.size(); i++) {
                    final TableRow tbrow = new TableRow(getApplicationContext());

                    TextView t1v = new TextView(getApplicationContext());
                    t1v.setText(list.get(i).CategoryName.toString());
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.CENTER);
                    t1v.setTextSize(18);
                    t1v.setPadding(5,20,5,5);
                    tbrow.addView(t1v);

                    ImageView t2v = new ImageView(getApplicationContext());
                    t2v.setPadding(5,20,5,5);
                    Glide.with(getApplicationContext())
                            .load(list.get(i).FilePath.toString()).into(t2v);
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

                        //    open(x);

                        }
                    });

                    t4v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String x = tbrow.getTag().toString();
                            Toast.makeText(getApplicationContext(),x,Toast.LENGTH_SHORT).show();
//                            getFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.editcat, new EditCategoryFragment())
//                                    .commit();
                        }
                    });

                    tableLayout.addView(tbrow);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
    }
}