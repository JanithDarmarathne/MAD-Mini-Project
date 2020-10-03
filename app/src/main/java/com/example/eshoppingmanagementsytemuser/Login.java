package com.example.eshoppingmanagementsytemuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText email,password;
    private Button btnLogin;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextTextEmailAddress2);
        password = findViewById(R.id.editTextTextPassword2);
        btnLogin = findViewById(R.id.button8);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Checking your credentials.....");
                progressDialog.show();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if(Email != null && Password != null){
                    mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(getApplicationContext(),"Successfully Logged",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),ProductsList.class);
                            startActivity(i);
                            // finish();
                            finish();
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Please Try Again later && check your credentials",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Please Enter your credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent i = new Intent(getApplicationContext(),ProductsList.class);
            startActivity(i);
            // finish();
            finish();
        }
    }
}