package com.example.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.bumptech.glide.Glide;
import com.example.dashboard.Models.Category;
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

public class Editcategory extends AppCompatActivity {

    EditText name;
    ImageView imageView;
    private DatabaseReference myRef,mDatabase;
    private Uri imageFilePath;
    private Bitmap imagetoStore,bit;
    private static final int PICK_IMAGE_REQUEST = 100;
    public ProgressDialog progressDialog,progressDialog1;
    ValueEventListener valueEventListener;
    ArrayList<Category> list;

    FirebaseStorage storage;
    StorageReference storageReference;
    String userId;
    String catName;
    String imageUrl;
    String pushKey;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcategory);

        name = findViewById(R.id.editTextTextPersonName2);
        imageView = findViewById(R.id.insertcateImage);
        btnUpdate = findViewById(R.id.button2);

        progressDialog = new ProgressDialog(this);
        progressDialog1 = new ProgressDialog(this);

        Intent i = getIntent();

        userId = i.getStringExtra("key");

        myRef = FirebaseDatabase.getInstance().getReference("category").child(userId);


        mDatabase = FirebaseDatabase.getInstance().getReference("category");
        InitListner();
        mDatabase.addValueEventListener(valueEventListener);
        list = new ArrayList<>();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                uploadImage(Name);
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Editcategory.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(i);

    }
    private void uploadImage(final String name1) {

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
                            Uri downloadUri = task.getResult();

                            Category cat = new Category(name1,downloadUri.toString(),pushKey);
                            myRef.setValue(cat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog1.dismiss();
                                        Toast.makeText(getApplicationContext(),"Successfully Updated the Category",Toast.LENGTH_SHORT).show();
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

                Category cat = new Category(name1,imageUrl,pushKey);
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

                    Category model = data.getValue(Category.class);
                    list.add(model);

                }

                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).pushKey.equals(userId)){
                        catName = list.get(i).CategoryName;
                        pushKey = list.get(i).pushKey;
                        imageUrl = list.get(i).FilePath;

                        name.setText(catName);
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