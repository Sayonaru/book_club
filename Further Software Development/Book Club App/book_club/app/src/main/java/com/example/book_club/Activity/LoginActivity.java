package com.example.book_club.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.book_club.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //view binding
    private ActivityLoginBinding binding;

    //firebaseauth
    private FirebaseAuth firebaseAuth;

    //Progress dialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //setup dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //handle click, go to register screen
        binding.noAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        //Begin Login
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

    }

    private String email = "", password = "";
    private void validateData() {
        //Before login, validate data
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();

        //Validation
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email pattern...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else {
            loginUser();
        }
    }

    private void loginUser() {
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        //Login user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Login Successful, check if user is user or admin
                        checkUser();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //Login Failure
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUser() {
        progressDialog.setMessage("Checking User");
        //Check if user or admin
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //check in db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        //Get user type
                        String userType = ""+snapshot.child("userType").getValue();
                        //check user type
                        //if (userType.equals("user")){
                            //this is simple user, open user dashboard
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        //}
                        //else if (userType.equals("admin")){
                        //    //this is admin, open admin dashboard
                        //    startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                        //    finish();
                        //}
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }
}