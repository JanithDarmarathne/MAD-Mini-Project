package com.example.dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Money extends AppCompatActivity {

    private EditText totalBal,tax,profit;
    private Button btnCal;

    double total,taxAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        totalBal = findViewById(R.id.editTextNumberDecimal);
        tax = findViewById(R.id.editTextNumberDecimal2);
        profit = findViewById(R.id.editTextNumberDecimal3);
        btnCal = findViewById(R.id.button7);



        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total  = Double.parseDouble(totalBal.getText().toString());
                taxAmount = Double.parseDouble(tax.getText().toString());
               double profileAmount = total - total * (taxAmount / 100) ;
               profit.setText(profileAmount+"");
            }
        });

    }
}