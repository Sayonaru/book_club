package com.example.session1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView newText;
    Button changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newText = findViewById(R.id.textView);
        changeButton = findViewById(R.id.firstButton);

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newText.setText("Find a great idea for the next app");
                Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
                startActivity(intent);
            }
        });
    }
}