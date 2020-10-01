package com.example.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dashboard.Models.Category;
import com.example.dashboard.Models.Help;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class edithelp extends AppCompatActivity {

    EditText helpname,help;
    Button btnUpdate;
    String  userId;
    public ProgressDialog progressDialog,progressDialog1;
    ValueEventListener valueEventListener;
    ArrayList<Help> list;
    private DatabaseReference myRef,mDatabase;
    String name , content,pushKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edithelp);

        helpname = findViewById(R.id.editTextTextPersonNamerdit);
        help = findViewById(R.id.edithelpconten);
        btnUpdate = findViewById(R.id.button4);

        progressDialog = new ProgressDialog(this);
        progressDialog1 = new ProgressDialog(this);

        Intent i = getIntent();

        userId = i.getStringExtra("key");

        myRef = FirebaseDatabase.getInstance().getReference("Help").child(userId);


        mDatabase = FirebaseDatabase.getInstance().getReference("Help");


        InitListner();
        mDatabase.addValueEventListener(valueEventListener);
        list = new ArrayList<>();


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String helpName = helpname.getText().toString();
                String helpContent = help.getText().toString();
                uploadImage(helpName,helpContent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(edithelp.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(i);

    }

    private void uploadImage(final String name,final String content) {

        if(name != null || content != null)
        {
            progressDialog1.setTitle("Updating...");
            progressDialog1.show();

            Help cat = new Help(name,content,pushKey);
            myRef.setValue(cat).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        progressDialog1.dismiss();
                        Toast.makeText(getApplicationContext(),"Successfully Updated the Help",Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog1.dismiss();
                        Toast.makeText(getApplicationContext(),"please try again later",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void InitListner(){
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Help model = data.getValue(Help.class);
                    list.add(model);

                }

                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).pushKey.equals(userId)){
                        name = list.get(i).HelpName;
                        content = list.get(i).HelpContent;
                        pushKey = list.get(i).pushKey;

                        helpname.setText(name);
                        help.setText(content);
                       // Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
    }
}