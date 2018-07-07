package com.sultani.erfan.tourplannerserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.rey.material.widget.CheckBox;
import com.sultani.erfan.tourplannerserver.Comman.Comman;
import com.sultani.erfan.tourplannerserver.Model.User;

import io.paperdb.Paper;

/**
 * Created by Sultani on 10/16/2017.
 */

public class Signin extends AppCompatActivity {

   EditText edtPhnumber, editPassword;
    Button btnSignin;

    FirebaseDatabase db;
    DatabaseReference users;

    CheckBox ckbRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);


        edtPhnumber = (EditText) findViewById(R.id.editphnumber);
        editPassword = (EditText) findViewById(R.id.editpassword);
        btnSignin = (Button) findViewById(R.id.btnsignin);



        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    signInUser(edtPhnumber.getText().toString(),editPassword.getText().toString());
            }


        });

    }

    private void signInUser(String phone, String password){



        final ProgressDialog progressDialog = new ProgressDialog(Signin.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();


        final String localPhone = phone;
        final String localPass = password;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(localPhone).exists()){

                    progressDialog.dismiss();
                    User user = dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    if (Boolean.parseBoolean(user.getIsStaff())){


                        if(user.getPassword().equals(localPass))
                        {
                                 //signin
                            Intent signin = new Intent(Signin.this,Home.class);
                            Comman.currentUser = user;
                            startActivity(signin);
                            finish();

                        }
                        else
                            Toast.makeText(Signin.this,"wrong password!",Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(Signin.this,"Please login with admin account",Toast.LENGTH_SHORT).show();





                }else {


                    progressDialog.dismiss();
                    Toast.makeText(Signin.this,"User not exist",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
