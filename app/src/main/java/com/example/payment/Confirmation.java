package com.example.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Confirmation extends AppCompatActivity {

    EditText address,Total;
    RadioButton radioButton,radioButton1,radioButton2,radioButton3;
    String payment;
    Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        address = findViewById(R.id.editTextTextPostalAddress);
        Total = findViewById(R.id.editTextNumber2);
        radioButton = findViewById(R.id.radioButton);
        radioButton1 = findViewById(R.id.radioButton2);
        radioButton2 = findViewById(R.id.radioButton3);
        radioButton3 = findViewById(R.id.radioButton4);
        btnOrder = findViewById(R.id.button3);


        if(radioButton.isChecked()){
            payment = "Visa";
        }else if(radioButton1.isChecked()){
            payment = "Master Card";
        }else if(radioButton2.isChecked()){
            payment = "Troy";
        }else if(radioButton3.isChecked()){
            payment="Web Money";
        }

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address1 = address.getText().toString();
            }
        });

    }
}