package com.example.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dashboard.Models.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class EditProduct extends AppCompatActivity {

    private EditText productName,ProductPrice,productMadeIN;
    private ImageView imageView;
    private Button btnUpdate;

    private DatabaseReference myRef,mDatabase;
    private Uri imageFilePath;
    private Bitmap imagetoStore,bit;
    private static final int PICK_IMAGE_REQUEST = 100;
    public ProgressDialog progressDialog,progressDialog1;
    ValueEventListener valueEventListener;
    ArrayList<Product> list;

    FirebaseStorage storage;
    StorageReference storageReference;
    String userId;
    String name;
    String imageUrl;
    String price;
    String madeIn;
    String pushKey;
    Uri downloadUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        imageView = findViewById(R.id.imageView8);
        productName = findViewById(R.id.editTextTextPersonName10);
        ProductPrice = findViewById(R.id.editTextTextPersonName11);
        productMadeIN = findViewById(R.id.editTextTextPersonName12);
        btnUpdate = findViewById(R.id.Update);

        Intent i = getIntent();

        userId = i.getStringExtra("key");

        progressDialog = new ProgressDialog(this);
        progressDialog1 = new ProgressDialog(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        myRef = FirebaseDatabase.getInstance().getReference("product").child(userId);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference("product");
        InitListner();
        mDatabase.addValueEventListener(valueEventListener);
        list = new ArrayList<Product>();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productname = productName.getText().toString();
                String price = ProductPrice.getText().toString();
                String madeIn = productMadeIN.getText().toString();
                if(productname != null && price != null && madeIn != null  ){
                    uploadImage(productname,price,madeIn);
                }else {
                    Toast.makeText(getApplicationContext(),"Please Add the image & Fill all Fields!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EditProduct.this,AddProduct.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(i);

    }

    private void uploadImage(final String name, final String price, final String madeIn) {

        if(imageFilePath != null || imageUrl != null)
        {
            progressDialog1.setTitle("Updating...");
            progressDialog1.show();


            if(imageFilePath != null){
                final StorageReference ref = storageReference.child("product/"+ UUID.randomUUID().toString());
                UploadTask uploadTask = ref.putFile(imageFilePath);

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadUri = task.getResult();

                            Product cat = new Product(name,price,madeIn,pushKey,downloadUri.toString());
                            myRef.setValue(cat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog1.dismiss();
                                        Toast.makeText(getApplicationContext(),"Successfully Updated the Product",Toast.LENGTH_SHORT).show();
                                    }else {
                                        progressDialog1.dismiss();
                                        Toast.makeText(getApplicationContext(),"please try again later",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // String pushKey = myRef.getKey().toString();
                        } else {

                        }
                    }
                });
            }else{

                Product cat = new Product(name,price,madeIn,pushKey,imageUrl);
                myRef.setValue(cat).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog1.dismiss();
                            Toast.makeText(getApplicationContext(),"Successfully Updated the Product",Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog1.dismiss();
                            Toast.makeText(getApplicationContext(),"please try again later",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }


    }

    public void chooseImage(){
        try{
            Intent i = new Intent();
            i.setType("image/");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i,PICK_IMAGE_REQUEST);
        }catch (Exception e){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageFilePath = data.getData();
            try {
                imagetoStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(imagetoStore);
            //Glide.with(this).load(imagetoStore).override(100,100).circleCrop().into(imageView);

        }
    }


    private void InitListner(){
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Product Models = data.getValue(Product.class);
                    list.add(Models);

                }

                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).pushKey.equals(userId)){
                        name = list.get(i).Name;
                        price = list.get(i).productPrice;
                        imageUrl = list.get(i).ImageUrl;
                        madeIn = list.get(i).productMade;
                        pushKey = list.get(i).pushKey;

                        productMadeIN.setText(madeIn);
                        productName.setText(name);
                        ProductPrice.setText(price);
                        Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
    }
}