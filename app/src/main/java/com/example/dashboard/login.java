package com.example.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
private Button button;
private EditText userName,passwordtext;
ProgressDialog progressDialog;
Toast toast;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.editTextmail);
        passwordtext = findViewById(R.id.editTextTextPassword);

        button = (Button) findViewById(R.id.loginbttn);

        progressDialog = new ProgressDialog(login.this);
        progressDialog.setTitle("SignIn");
        progressDialog.setMessage("Checking Your Credentials...");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = userName.getText().toString();
                String password = passwordtext.getText().toString();

                progressDialog.show();

                try{
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(login.this,new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        toast = Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER_VERTICAL,-10,0);
                                        toast.show();
                                        openMainActivity();
                                        progressDialog.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                }
                            });
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Getting Error Try again! "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
        if(currentUser != null){

            openMainActivity();
        }
    }
}