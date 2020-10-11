package com.example.dashboard;

import android.app.ProgressDialog;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dashboard.Models.Category;
import com.example.dashboard.Models.Help;
import com.example.dashboard.Models.Product;
import com.example.dashboard.Models.Setting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashFragment extends Fragment {


    private DatabaseReference mDatabase,mDatabase1,mDatabase2,mDatabase3,myRef;
    ValueEventListener valueEventListener,valueEventListener1,valueEventListener2,valueEventListener3,valueEventListener4;
    View view;
    private ArrayList<Category> list;
    private ArrayList<Help> list1;
    private ArrayList<Product> list2;
    private TextView pending,procceed,canceled,total,catNum,productNum,HelpNum,adminTxt,tax;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dash, container, false);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pending = view.findViewById(R.id.pendText);
        procceed = view.findViewById(R.id.procestext);
        canceled = view.findViewById(R.id.canceltxt);
        total =  view.findViewById(R.id.totalText);
        catNum = view.findViewById(R.id.catNum);
        productNum = view.findViewById(R.id.produNum);
        HelpNum = view.findViewById(R.id.helpNum);
        adminTxt = view.findViewById(R.id.editTextNumber3);
        tax = view.findViewById(R.id.txtedite1);


        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list2= new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("category");
        mDatabase1 = FirebaseDatabase.getInstance().getReference("Help");
        mDatabase2 = FirebaseDatabase.getInstance().getReference("product");
        mDatabase3 = FirebaseDatabase.getInstance().getReference("Admin");
        myRef = FirebaseDatabase.getInstance().getReference("Setting");
        InitListner();
        mDatabase.addValueEventListener(valueEventListener);
        InitListner1();
        mDatabase1.addValueEventListener(valueEventListener1);
        InitListner2();
        mDatabase2.addValueEventListener(valueEventListener2);
        InitListner3();
        mDatabase3.addValueEventListener(valueEventListener3);
        InitListner4();
        myRef.addValueEventListener(valueEventListener4);

    }

    private void InitListner(){
//        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("Loading...");
//        progressDialog.show();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Category model = data.getValue(Category.class);
                    list.add(model);

                }



                int i = list.size();

                catNum.setText(i+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
    }

    private void InitListner1(){
//        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("Loading...");
//        progressDialog.show();

        valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Help model = data.getValue(Help.class);
                    list1.add(model);

                }



                int i = list1.size();

                HelpNum.setText(i+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
    }

    private void InitListner2(){
//        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("Loading...");
//        progressDialog.show();

        valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Product model = data.getValue(Product.class);
                    list2.add(model);

                }



                int i = list2.size();

                productNum.setText(i+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
    }

    private void InitListner3(){
//        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("Loading...");
//        progressDialog.show();

        valueEventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Help model = data.getValue(Help.class);
                    list1.add(model);

                }



                int i = list1.size();

                adminTxt.setText(i+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
    }

    private void InitListner4(){
//        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("Loading...");
//        progressDialog.show();

        valueEventListener4 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Setting setting = dataSnapshot.getValue(Setting.class);

                tax.setText(setting.Tax+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
    }
}