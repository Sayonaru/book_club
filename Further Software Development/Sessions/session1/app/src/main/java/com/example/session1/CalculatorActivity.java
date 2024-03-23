package com.example.session1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.session1.databinding.ActivityCalculatorBinding;

public class CalculatorActivity extends AppCompatActivity {

    private ActivityCalculatorBinding binding;
    private String number1;
    private int intNum1;
    private String number2;
    private int intNum2;
    private String intNumTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.editTextNumber.getText().toString().isEmpty()){
                    if(!binding.editTextNumber2.getText().toString().isEmpty()){
                        number1 = binding.editTextNumber.getText().toString();
                        intNum1 = Integer.parseInt(number1);
                        number2 = binding.editTextNumber2.getText().toString();
                        intNum2 = Integer.parseInt(number2);
                        intNumTotal = String.valueOf(intNum1 + intNum2);
                        binding.resultTv.setText(intNumTotal);
                    }
                }
            }
        });

        binding.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.editTextNumber.getText().toString().isEmpty()){
                    if(!binding.editTextNumber2.getText().toString().isEmpty()){
                        number1 = binding.editTextNumber.getText().toString();
                        intNum1 = Integer.parseInt(number1);
                        number2 = binding.editTextNumber2.getText().toString();
                        intNum2 = Integer.parseInt(number2);
                        intNumTotal = String.valueOf(intNum1 - intNum2);
                        binding.resultTv.setText(intNumTotal);
                    }
                }
            }
        });

        binding.divideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.editTextNumber.getText().toString().isEmpty()){
                    if(!binding.editTextNumber2.getText().toString().isEmpty()){
                        number1 = binding.editTextNumber.getText().toString();
                        intNum1 = Integer.parseInt(number1);
                        number2 = binding.editTextNumber2.getText().toString();
                        intNum2 = Integer.parseInt(number2);
                        intNumTotal = String.valueOf(intNum1 / intNum2);
                        binding.resultTv.setText(intNumTotal);
                    }
                }
            }
        });

        binding.multBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.editTextNumber.getText().toString().isEmpty()){
                    if(!binding.editTextNumber2.getText().toString().isEmpty()){
                        number1 = binding.editTextNumber.getText().toString();
                        intNum1 = Integer.parseInt(number1);
                        number2 = binding.editTextNumber2.getText().toString();
                        intNum2 = Integer.parseInt(number2);
                        intNumTotal = String.valueOf(intNum1 * intNum2);
                        binding.resultTv.setText(intNumTotal);
                    }
                }
            }
        });
    }
}