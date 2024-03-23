package com.example.book_club.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_club.Adapter.AdapterForum;
import com.example.book_club.Domain.ModelForum;
import com.example.book_club.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BookForumActivity extends AppCompatActivity {

    private String currentForumName, currentBookId;

    private ProgressDialog progressDialog;

    TextView titleTv;
    String bookId;
    ImageButton sendBtn, backBtn;
    EditText inputMessage;
    RecyclerView messagesRv;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelForum> forumArrayList;
    private AdapterForum adapterForum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_forum);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();

        //currentForumName = getIntent().getExtras().get(currentForumName).toString();
        currentBookId = getIntent().getStringExtra("currentBookId");

        sendBtn = findViewById(R.id.send_message_button);
        inputMessage = findViewById(R.id.input_forum_message);
        messagesRv = findViewById(R.id.messagesRv);
        backBtn = findViewById(R.id.backBtn);

        titleTv = findViewById(R.id.titleTv);
        bookId = currentBookId;

        titleTv.setText(currentForumName);
        loadMessages();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addForumDialog();
            }
        });

    }

    private void loadMessages() {
        forumArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FavBooks");
        ref.child(bookId).child("ForumMessages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        forumArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            ModelForum model = ds.getValue(ModelForum.class);
                            forumArrayList.add(model);
                        }
                        //setupadapter
                        adapterForum = new AdapterForum(BookForumActivity.this, forumArrayList);
                        messagesRv.setAdapter(adapterForum);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String message = "";
    private void addForumDialog() {
        message = inputMessage.getText().toString().trim();
        //validate
        if (TextUtils.isEmpty(message)){
            Toast.makeText(this, "Enter your message...", Toast.LENGTH_SHORT).show();
        }
        else {
            addMessage();
        }
    }

    private void addMessage() {

        progressDialog.setMessage("Adding message...");
        progressDialog.show();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        String timestamp = ""+System.currentTimeMillis();

        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("id", ""+timestamp);
        hashmap.put("bookId", ""+bookId);
        hashmap.put("timestamp", timestamp);
        hashmap.put("message", message);
        hashmap.put("uid", ""+firebaseUser.getUid());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FavBooks");
        ref.child(bookId).child("ForumMessages").child(timestamp)
                .setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(BookForumActivity.this, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(BookForumActivity.this, "Failed to send message due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


}