package com.example.session2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.session2.databinding.ActivityJobApplyBinding;

public class JobApplyActivity extends AppCompatActivity {

    EditText firstName, lastName, Dob, Country;
    Button apply;
    private ActivityJobApplyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobApplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.idFirstNameEt.getText().toString().isEmpty()){
                    if(!binding.idLastNameEt.getText().toString().isEmpty()){
                        if(!binding.idDobEt.getText().toString().isEmpty()){
                            if(!binding.idCountryEt.getText().toString().isEmpty()){
                                Toast.makeText(JobApplyActivity.this, "Job Application sent successfully!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(JobApplyActivity.this, "Please enter country", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(JobApplyActivity.this, "Please enter date of birth", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(JobApplyActivity.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(JobApplyActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}