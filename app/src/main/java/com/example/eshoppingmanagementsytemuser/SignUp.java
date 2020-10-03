package com.example.eshoppingmanagementsytemuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private Button btnsignUp;
    private EditText firstName,lastName,email,password,confirmPassword;
    boolean pwsCheck;
    FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = findViewById(R.id.editTextTextPersonName);
        lastName = findViewById(R.id.editTextTextPersonName2);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        confirmPassword = findViewById(R.id.editTextTextPassword3);
        btnsignUp = findViewById(R.id.button2);

        mAuth = FirebaseAuth.getInstance();

        myRef = FirebaseDatabase.getInstance().getReference("Users");
        progressDialog = new ProgressDialog(this);

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String p1 = password.getText().toString();
                String p2 = confirmPassword.getText().toString();

                if(!p1.equals(p2)){
                    // Toast.makeText(MainActivity.this,"Password not match", Toast.LENGTH_SHORT).show();
                    pwsCheck = true;
                }else{
                    //  Toast.makeText(MainActivity.this,"Password is matched", Toast.LENGTH_SHORT).show();
                    pwsCheck = true;;
                }
            }
        });

        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String firstname = firstName.getText().toString();
                final String lastname = lastName.getText().toString();
                final String Email = email.getText().toString();
                String Password = password.getText().toString();
                progressDialog.setTitle("Loading....");
                try {

                    progressDialog.show();
                    if(pwsCheck){
                        if(firstname != null && lastname != null && Email != null && Password != null){

                            mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        HashMap<String,String> map = new HashMap<>();
                                        map.put("FirstName",firstname);
                                        map.put("LastName",lastname);
                                        map.put("Email",Email);

                                        myRef.child(firstname+" "+lastname).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(SignUp.this,"SuccessFully created the Account", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(getApplicationContext(),ProductsList.class);
                                                    startActivity(i);
                                                    finish();
                                                }else{
                                                    Toast.makeText(SignUp.this,"Please Try again later", Toast.LENGTH_SHORT).show();
                                                }

                                                progressDialog.dismiss();
                                            }

                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignUp.this,"Please Check your details", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                }
                            });

                        }else{
                            Toast.makeText(getApplicationContext(),"Please Fill the all fields!!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Password not matched",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){

                   progressDialog.show();
                }
            }
        });
    }
}