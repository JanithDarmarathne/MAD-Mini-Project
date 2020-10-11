package com.example.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dashboard.Models.User;
import com.example.dashboard.services.SharedPrefManager;
import com.example.dashboard.services.ShopDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private EditText edtEmail,edtPassword;
    private Button btnLogin;
    private TextView createAccount;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtEmail = findViewById(R.id.editTextTextEmailAddress2);
        edtPassword = findViewById(R.id.editTextTextPassword3);
        btnLogin = findViewById(R.id.button2);
        createAccount = findViewById(R.id.textView23);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
               // Toast.makeText(getApplicationContext(),"came "+email+" "+password,Toast.LENGTH_LONG).show();
                try{
                   if(email == null || password == null){
                       Toast.makeText(getApplicationContext(),"Please enter your credentials!",Toast.LENGTH_SHORT).show();
                   }else{
                       ShopDB db = new ShopDB(getApplicationContext());
                       db.open();
                       int i = db.getSelectedUser(email,password);

                       if(i == 1){
                         //  Toast.makeText(getApplicationContext(),"came here",Toast.LENGTH_SHORT).show();
                           User user = db.getUser(email);
                           SharedPrefManager.getInstance(getApplicationContext())
                                   .userLogin(user.Name,user.Email,user.Address);

                           mAuth.signInAnonymously()
                                   .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                                       @Override
                                       public void onComplete(@NonNull Task<AuthResult> task) {
                                           if (task.isSuccessful()) {
                                               // Sign in success, update UI with the signed-in user's information
                                               //Log.d(TAG, "signInAnonymously:success");
                                             //  FirebaseUser user = mAuth.getCurrentUser();
                                             //  updateUI(user);
                                               Intent intent = new Intent(getApplicationContext(), Account.class);
                                               startActivity(intent);
                                               finish();
                                               Toast.makeText(getApplicationContext(),"Successfully logged into your account!",Toast.LENGTH_SHORT).show();
                                           } else {
                                               // If sign in fails, display a message to the user.
                                             //  Log.w(TAG, "signInAnonymously:failure", task.getException());
                                              // Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.",
                                                   //    Toast.LENGTH_SHORT).show();
                                             //  updateUI(null);
                                               Toast.makeText(getApplicationContext(),"Please Try Again!",Toast.LENGTH_SHORT).show();
                                           }

                                           // ...
                                       }
                                   });
                         //  Toast.makeText(getApplicationContext(),"came here "+user.Name+" "+user.Email,Toast.LENGTH_SHORT).show();

                       }else{
                           Toast.makeText(getApplicationContext(),"Please check your credentials"+i,Toast.LENGTH_SHORT).show();
                       }
                       db.close();
                   }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivityseller.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(SharedPrefManager.getInstance(getApplicationContext())
                .isLoggedIn() && user == null){
            Intent intent = new Intent(getApplicationContext(), Account.class);
            startActivity(intent);
            finish();
        }
    }
}