package com.example.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class editHelpFragment extends Fragment {

    private EditText helpName,helpContent; //editTextTextPersonNamerdit edithelpcon

    private Button btnsAVE;

    private DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_help, container, false);

        helpName = v.findViewById(R.id.editTextTextPersonNamerdit);
        helpContent = v.findViewById(R.id.edithelpconten);
        btnsAVE = v.findViewById(R.id.button4);

        myRef = FirebaseDatabase.getInstance().getReference("Help");

        btnsAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = helpName.getText().toString();
                String content = helpContent.getText().toString();

                HashMap<String,String> map = new HashMap<>();
                map.put("Name",name);
                map.put("Content",content);

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                myRef.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"Successfully Added the Help",Toast.LENGTH_SHORT);
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"please try again later",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}