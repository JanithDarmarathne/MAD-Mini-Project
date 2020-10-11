package com.example.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginWelcomePage extends AppCompatActivity {
    private Button btnLogin,btnSignUp;
    TextView sellerlg,adminlog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_welcome_page);
        btnLogin = findViewById(R.id.button7);
        btnSignUp = findViewById(R.id.button3);
        sellerlg = findViewById(R.id.sellerlg);
        adminlog = findViewById(R.id.adminlg);

        sellerlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });

        adminlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdminActivity();
            }
        });
        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignUp.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginUser.class);
                startActivity(i);
            }
        });
    }

    private void openAdminActivity() {
        Intent intent = new Intent(this,login.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this,SignIn.class);
        startActivity(intent);
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            Intent i = new Intent(getApplicationContext(),ProductsList.class);
            startActivity(i);
            finish();
        }
    }

 */
}