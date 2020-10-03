package com.example.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seller.models.Product;
import com.example.seller.services.ShopDB;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Comment extends AppCompatActivity {

    ArrayList<com.example.seller.models.Comment> list;
    private Button btnProduct;
    private TextInputLayout productName,productCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        btnProduct = findViewById(R.id.button6);


        list = new ArrayList<com.example.seller.models.Comment>();
        TableLayout stk = findViewById(R.id.tableLayout1);
        ShopDB db = new ShopDB(getApplicationContext());
        db.open();
        list = db.getComment();
//        Toast.makeText(getApplicationContext(),list.size()+list.get(2).Name,Toast.LENGTH_LONG).show();
        // db.close();
        for (int i = 0; i < list.size(); i++) {
            final TableRow tbrow = new TableRow(this);

            TextView t1v = new TextView(this);
            t1v.setText(list.get(i).Comment.toString());
            t1v.setTextColor(Color.BLACK);
            t1v.setBackgroundColor(Color.LTGRAY);
            t1v.setGravity(Gravity.LEFT);
            t1v.setWidth(650);
            t1v.setHeight(90);
            t1v.setTextSize(18);
            t1v.setPadding(10,20,5,5);
            tbrow.setPadding(15,20,0,5);
            tbrow.addView(t1v);


            stk.addView(tbrow);
        }


        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog();
            }
        });
    }

    private void myDialog(){
        final AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        myDialog.setTitle("Add Comment");

        LayoutInflater inflater = LayoutInflater.from(this);
        View myCustomlayout = inflater.inflate(R.layout.custom_product,null);

        productName = myCustomlayout.findViewById(R.id.productName);
        productName.setHint("Comment");

        productCategory = myCustomlayout.findViewById(R.id.productCategory);
        productCategory.setVisibility(View.GONE);

        myDialog.setView(myCustomlayout);

        myDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                final String productNam = productName.getEditText().getText().toString();
              //  final String productCat = productCategory.getEditText().getText().toString();

                if(productNam.length()>0){
                    addComment(productNam);

                    dialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(),"Please add the details",Toast.LENGTH_SHORT).show();
                }

            }

        });

        myDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        myDialog.show();
    }

    private void addComment(final String productName){
        try{
            ShopDB db = new ShopDB(getApplicationContext());
            db.open();
            db.addComment(productName);
            db.close();
            Toast.makeText(getApplicationContext(),"Successfully Added the Comment!",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Try again later",Toast.LENGTH_SHORT).show();
        }
    }
}