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

import com.example.dashboard.Models.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddProducts extends AppCompatActivity {

    private EditText productName,productPrice,productMade;
    private ImageView productImage;
    private Button btnSave;

    private DatabaseReference myRef,myRef1;
    private Uri imageFilePath;
    private Bitmap imagetoStore,bit;
    private static final int PICK_IMAGE_REQUEST = 100;
    public ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        productImage = findViewById(R.id.imageView6);
        productName = findViewById(R.id.editTextTextPersonName4);
        productPrice = findViewById(R.id.editTextTextPersonName5);
        productMade = findViewById(R.id.editTextTextPersonName6);
        btnSave = findViewById(R.id.button4);
        progressDialog = new ProgressDialog(this);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        myRef = FirebaseDatabase.getInstance().getReference("product").push();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productname = productName.getText().toString();
                String price = productPrice.getText().toString();
                String madeIn = productMade.getText().toString();
                
                if(productname != null && price != null && madeIn != null && imageFilePath != null ){
                    uploadImage(productname,price,madeIn);
                }else {
                    Toast.makeText(getApplicationContext(),"Please Add the image & Fill all Fields!",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    private void uploadImage(final String name,final String price,final String madeIn) {

        if(imageFilePath != null)
        {

            progressDialog.setTitle("Uploading...");
            progressDialog.show();

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

                        String pushKey = myRef.getKey().toString();

                       Product cat = new Product(name,price,madeIn,pushKey,downloadUri.toString());

                        myRef.setValue(cat).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Successfully Added the Product",Toast.LENGTH_SHORT);
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"please try again later",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        // Handle failures
                        // ...

                    }
                }
            });
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

            productImage.setImageBitmap(imagetoStore);
            //Glide.with(this).load(imagetoStore).override(100,100).circleCrop().into(imageView);

        }
    }

}
