package com.example.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dashboard.services.ShopDB;

import java.io.IOException;

public class MainActivityseller extends AppCompatActivity {

    private EditText editName,editAddress,editEmail,editPassword,editConfirmPassword;
    private Button btnSignUp;
    boolean pwsCheck = false;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri imageFilePath;
    private Bitmap imagetoStore,bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainseller);

        editName = findViewById(R.id.editTextTextPersonName);
        editAddress = findViewById(R.id.editTextTextPersonName2);
        editEmail = findViewById(R.id.editTextTextEmailAddress);
        editPassword = findViewById(R.id.editTextTextPassword);
        editConfirmPassword = findViewById(R.id.editTextTextPassword2);
        imageView = findViewById(R.id.imageView);
        btnSignUp = findViewById(R.id.button);


        editConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String p1 = editPassword.getText().toString();
                String p2 = editConfirmPassword.getText().toString();

                if(!p1.equals(p2)){
                   // Toast.makeText(MainActivity.this,"Password not match", Toast.LENGTH_SHORT).show();
                    pwsCheck = true;
                }else{
                  //  Toast.makeText(MainActivity.this,"Password is matched", Toast.LENGTH_SHORT).show();
                    pwsCheck = true;;
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                String email = editEmail.getText().toString();
                String address = editAddress.getText().toString();
                String password = editPassword.getText().toString();
                try{
                    if(pwsCheck){
                        if(imagetoStore != null){
                            ShopDB db = new ShopDB(getApplicationContext());
                            db.open();
                            long i = db.CreateUser(name,email,address,password,imagetoStore);
                            db.close();
                            if(i != 0){
                                Toast.makeText(MainActivityseller.this,"Successfully created your account!",Toast.LENGTH_SHORT).show();
                                editName.setText("");
                                editEmail.setText("");
                                editAddress.setText("");
                                editPassword.setText("");
                                editConfirmPassword.setText("");
                                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(MainActivityseller.this,"Please Try Again!",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivityseller.this,"Please select the profile picture",Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(MainActivityseller.this,"Your password not matched with confirm password",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toast.makeText(MainActivityseller.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

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

            //imageView.setImageBitmap(imagetoStore);
            Glide.with(this).load(imagetoStore).override(100,100).circleCrop().into(imageView);

        }
    }


    public void chooseImage(View view){
        try{

            Intent i = new Intent();
            i.setType("image/");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i,PICK_IMAGE_REQUEST);

        }catch (Exception e){

        }
    }

}