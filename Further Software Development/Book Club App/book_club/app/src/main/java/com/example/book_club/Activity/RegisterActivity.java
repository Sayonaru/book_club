package com.example.book_club.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.book_club.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //view binding
    private ActivityRegisterBinding binding;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;

    //Progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        //Setup Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //handle click, go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //handle click, begin register
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.haveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private String name = "", email = "", password = "";
    private void validateData() {
        //before crating account, data validation

        //get data
        name = binding.nameEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();
        String cPassword = binding.cPasswordEt.getText().toString().trim();

        //Validate data
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter your name...", Toast.LENGTH_SHORT).show();
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid Email Address...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter Password...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cPassword)){
            Toast.makeText(this, "Confirm Password...", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(cPassword)){
            Toast.makeText(this, "Passwords Don't Match...", Toast.LENGTH_SHORT).show();
        }
        else {
            //All data valid, create account
            createUserAccount();
        }
    }

    private void createUserAccount() {
        //Show Progress
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        //Create User In Firebase Auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Account Creation Successful, now add in firebase realtime database
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //Account Creating Failed
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserInfo() {
        progressDialog.setMessage("Saving User Info...");

        //Timestamp
        long timestamp = System.currentTimeMillis();

        //get current user uid, since user is registered so we can get now
        String uid = firebaseAuth.getUid();

        //setup data to add in db
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("uid", uid);
        hashmap.put("email", email);
        hashmap.put("name", name);
        hashmap.put("profileImage", "");//add empty, will do later
        hashmap.put("userType", "user"); //possible values are user and admin, will make admin manually in firebase realtime database by changing this value
        hashmap.put("timestamp", timestamp);

        //set data to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //data added to db
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Account Created...", Toast.LENGTH_SHORT).show();
                        //since user account is created so start dashboard of user
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}