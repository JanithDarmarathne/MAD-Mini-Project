package com.example.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dashboard.Models.Category;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddcateFragment extends Fragment {


    private EditText textl;
    private ImageView imageView;
    private Button btn;
    private DatabaseReference myRef,myRef1;
    private Uri imageFilePath;
    private Bitmap imagetoStore,bit;
    private static final int PICK_IMAGE_REQUEST = 100;

    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_addcate, container, false);
        textl = v.findViewById(R.id.editTextTextPersonName2);
        imageView = v.findViewById(R.id.insertcateImage);
        btn = v.findViewById(R.id.button3);

        myRef = FirebaseDatabase.getInstance().getReference("category").push();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String category = textl.getText().toString();

              if(category != null && imageFilePath != null){

                  uploadImage(category);

              }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        return  v;
    }

    private void uploadImage(final String category) {

        if(imageFilePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
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

                        Category cat = new Category(category,downloadUri.toString(),pushKey);

                        myRef.setValue(cat).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),"Successfully Added the category",Toast.LENGTH_SHORT);
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),"please try again later",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageFilePath = data.getData();
            try {
                imagetoStore = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(imagetoStore);
            //Glide.with(this).load(imagetoStore).override(100,100).circleCrop().into(imageView);

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
}