package com.example.dashboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dashboard.Models.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class ListcateFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ValueEventListener valueEventListener;
    View view;
    private ArrayList<Category> list;
    private TableLayout tableLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_listcate, container, false);

        return  view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        tableLayout = view.findViewById(R.id.table);

        mDatabase = FirebaseDatabase.getInstance().getReference("category");
        InitListner();
        mDatabase.addValueEventListener(valueEventListener);
        list = new ArrayList<>();


    }

    private void InitListner(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Category model = data.getValue(Category.class);
                    list.add(model);

                }

                for (int i = 0; i < list.size(); i++) {
                    final TableRow tbrow = new TableRow(getContext());

                    TextView t1v = new TextView(getContext());
                    t1v.setText(list.get(i).CategoryName.toString());
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.CENTER);
                    t1v.setTextSize(18);
                    t1v.setPadding(5,20,5,5);
                    tbrow.addView(t1v);

                    ImageView t2v = new ImageView(getContext());
                    t2v.setPadding(5,20,5,5);
                    Glide.with(getContext())
                            .load(list.get(i).FilePath.toString()).into(t2v);
                    t2v.setMaxWidth(10);
                    t2v.setMaxHeight(10);
                    tbrow.addView(t2v);

                    TextView t3v = new TextView(getContext());
                    t3v.setText("Delete");
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.CENTER);
                    t3v.setPadding(40,20,0,5);
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(getContext());
                    t4v.setText("Edit");
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.CENTER);
                    t4v.setPadding(0,20,0,5);
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
                            Intent i = new Intent(getContext(),Editcategory.class);
                            i.putExtra("key",x);
                            startActivity(i);
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

    public void open(final String id){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Delete Product");
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //   deleteProduct(id);
                        mDatabase.child(id).removeValue();
                        Toast.makeText(getContext(),"Successfully deleted the item",Toast.LENGTH_SHORT).show();
                        //startActivity(getActivity().getIntent());
                        getActivity().finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                //  alertDialogBuilder.
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}