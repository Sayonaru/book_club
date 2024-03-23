package com.example.book_club.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_club.Adapter.AdapterComment;
import com.example.book_club.Domain.ModelComment;
import com.example.book_club.R;
import com.example.book_club.databinding.ActivityBookDetailsBinding;
import com.example.book_club.databinding.DialogCommentAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class BookDetails extends AppCompatActivity {

    // creating variables for strings,text view, image views and button.
    String title, subtitle, publisher, publishedDate, description, thumbnail, previewLink, infoLink, buyLink, bookId;
    int pageCount;
    private ArrayList<String> authors;

    TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV;
    Button previewBtn, buyBtn, favBtn, forumBtn;
    ImageButton idCommentBtn, backBtn;
    RecyclerView commentsRv;
    private ImageView bookIV;
    private ActivityBookDetailsBinding binding;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ArrayList<ModelComment> commentArrayList;
    private AdapterComment adapterComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityBookDetailsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_book_details);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        //setContentView(binding.getRoot());

        // initializing our views..
        titleTV = findViewById(R.id.idTVTitle);
        subtitleTV = findViewById(R.id.idTVSubTitle);
        publisherTV = findViewById(R.id.idTVpublisher);
        descTV = findViewById(R.id.idTVDescription);
        pageTV = findViewById(R.id.idTVNoOfPages);
        publishDateTV = findViewById(R.id.idTVPublishDate);
        previewBtn = findViewById(R.id.idBtnPreview);
        buyBtn = findViewById(R.id.idBtnBuy);
        bookIV = findViewById(R.id.idIVbook);
        favBtn = findViewById(R.id.idBtnFav);
        idCommentBtn = findViewById(R.id.idCommentBtn);
        commentsRv = findViewById(R.id.commentsRv);
        forumBtn = findViewById(R.id.idBtnForum);
        backBtn = findViewById(R.id.backBtn);

        //forumBtn.setVisibility(View.GONE);

        // getting the data which we have passed from our adapter class.
        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        publisher = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");
        previewLink = getIntent().getStringExtra("previewLink");
        infoLink = getIntent().getStringExtra("infoLink");
        buyLink = getIntent().getStringExtra("buyLink");
        bookId = getIntent().getStringExtra("bookId");

        String currentForumName = title.toString();
        String currentBookId = bookId.toString();

        // after getting the data we are setting
        // that data to our text views and image view.
        titleTV.setText(title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Published On : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("No Of Pages : " + pageCount);
        //Picasso.get().load(thumbnail).into(bookIV);

        loadComments();

        // adding on click listener for our preview button.
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewLink.isEmpty()) {
                    // below toast message is displayed when preview link is not present.
                    Toast.makeText(BookDetails.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if the link is present we are opening
                // that link via an intent.
                Uri uri = Uri.parse(previewLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // initializing on click listener for buy button.
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyLink.isEmpty()) {
                    // below toast message is displaying when buy link is empty.
                    Toast.makeText(BookDetails.this, "No buy page present for this book", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if the link is present we are opening
                // the link via an intent.
                Uri uri = Uri.parse(buyLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFavouriteBook();
            }
        });

        forumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String currentForumName = title.toString();
                //String currentBookId = bookId.toString();
                Intent bookForumIntent = new Intent(BookDetails.this, BookForumActivity.class);
                bookForumIntent.putExtra("currentForumName", title);
                bookForumIntent.putExtra("currentBookId", bookId);
                startActivity(bookForumIntent);
            }
        });

        idCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentDialog();
            }
        });
    }

    private void loadComments() {
        commentArrayList = new ArrayList<>();

        //db path to load comments
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FavBooks");
        ref.child(bookId).child("Comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clear arraylist before adding data
                        commentArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            //get data as model
                            ModelComment model = ds.getValue(ModelComment.class);
                            //add to arrayList
                            commentArrayList.add(model);
                        }
                        //setup adapter
                        adapterComment = new AdapterComment(BookDetails.this, commentArrayList);
                        //set adapter to recyclerview
                        commentsRv.setAdapter(adapterComment);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String comment = "";
    private void addCommentDialog() {
        //inflate bind view for dialog
        DialogCommentAddBinding commentAddBinding = DialogCommentAddBinding.inflate(LayoutInflater.from(this));

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        builder.setView(commentAddBinding.getRoot());

        //create and show alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        commentAddBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        commentAddBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data
                comment = commentAddBinding.commentEt.getText().toString().trim();
                //validate data
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(BookDetails.this, "Enter your comment...", Toast.LENGTH_SHORT).show();
                }
                else {
                    alertDialog.dismiss();
                    addComment();
                }
            }
        });

    }

    private void addComment() {
        progressDialog.setMessage("Adding comment...");
        progressDialog.show();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //timestamp for comment id, comment time
        String timestamp = ""+System.currentTimeMillis();

        //setup data to add in db for comment
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("id", ""+timestamp);
        hashmap.put("bookId", ""+bookId);
        hashmap.put("timestamp", ""+timestamp);
        hashmap.put("comment", ""+comment);
        hashmap.put("uid", ""+firebaseUser.getUid());

        //DB path to add data into it
        //
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FavBooks");
        ref.child(bookId).child("Comments").child(timestamp)
                .setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(BookDetails.this, "Comment Added Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to add comment
                        progressDialog.dismiss();
                        Toast.makeText(BookDetails.this, "Failed to add comment due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkFavouriteBook() {
        progressDialog.setMessage("Checking if already in database...");
        progressDialog.show();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference bookIdRef = rootRef.child("FavBooks");

        bookIdRef.child(bookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    checkSavedUid();
                }
                else {
                    addFavouriteBook();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkSavedUid() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("FavBooks").child(bookId);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        usersRef.child("Saved").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != firebaseUser.getUid()){
                    addSavedUid();
                }
                else{
                    progressDialog.setMessage("Successfully added uid");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addFavouriteBook() {
        progressDialog.setMessage("Adding Favourite Book...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();

        //setup info to add in firebase db
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("bookId", ""+bookId);
        //hashmap.put("id", ""+timestamp);
        hashmap.put("title", ""+title);
        hashmap.put("description", ""+description);
        hashmap.put("publisher", ""+publisher);
        hashmap.put("pageCount", ""+pageCount);
        hashmap.put("publishDate", ""+publishedDate);
        hashmap.put("thumbnail", ""+thumbnail);
        hashmap.put("previewLink", ""+previewLink);
        hashmap.put("buyLink", ""+buyLink);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FavBooks");
        ref.child(""+bookId)
                .setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(BookDetails.this, "Favourite book added successfully", Toast.LENGTH_SHORT).show();
                        addSavedUid();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(BookDetails.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addSavedUid() {
        progressDialog.setMessage("Adding Uid...");
        progressDialog.show();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String timestamp = ""+System.currentTimeMillis();

        //setup info to add in firebase db
        HashMap<String, Object> Hashmap = new HashMap<>();
        Hashmap.put("uid", ""+firebaseUser.getUid());




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("FavBooks");
        reference.child(bookId).child("Saved").child(firebaseUser.getUid())
                .setValue(Hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(BookDetails.this, "Uid added successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        checkSavedBookId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(BookDetails.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkSavedBookId() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        ref.child(firebaseUser.getUid()).child("books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != bookId){
                    addSavedBookId();
                }
                else{
                    progressDialog.setMessage("Successfully saved book");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addSavedBookId() {
        progressDialog.setMessage("Adding Book Id to user...");
        progressDialog.show();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String timestamp = ""+System.currentTimeMillis();

        //setup info to add in firebase db
        HashMap<String, Object> Hashmap = new HashMap<>();
        Hashmap.put("savedBookId", ""+bookId);
        //Hashmap.put("savedBookTitle", ""+title);
        //Hashmap.put("savedBookDate", ""+publishedDate);
        //Hashmap.put("savedPageCount", ""+pageCount);




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseUser.getUid()).child("books")
                .setValue(Hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(BookDetails.this, "bookId added successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(BookDetails.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
