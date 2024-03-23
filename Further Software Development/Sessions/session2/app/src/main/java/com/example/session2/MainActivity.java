package com.example.session2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button sendButton, nextButton;
    EditText toText, subjectText, messageText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toText = findViewById(R.id.idToEt);
        subjectText = findViewById(R.id.idSubjectEt);
        messageText = findViewById(R.id.idMessageEt);
        sendButton = findViewById(R.id.sendBtn);
        nextButton = findViewById(R.id.nextBtn);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JobApplyActivity.class);
                startActivity(intent);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!toText.getText().toString().isEmpty()){
                    if(!subjectText.getText().toString().isEmpty()){
                        if(!messageText.getText().toString().isEmpty()){
                            Toast.makeText(MainActivity.this, "Message Sent!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Please enter your message!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Please enter your subject!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Please enter who you are sending the message to!", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
    }
}