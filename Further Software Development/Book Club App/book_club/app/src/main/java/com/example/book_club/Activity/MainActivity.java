package com.example.book_club.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.book_club.Adapter.BookAdapter;
import com.example.book_club.Adapter.SavedBookHistoryAdapter;
import com.example.book_club.Domain.BookInfo;
import com.example.book_club.Domain.SavedInfo;
import com.example.book_club.MyApplication;
import com.example.book_club.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private ArrayList<BookInfo> bookInfoArrayList;
    private ArrayList<SavedInfo> savedInfoArrayList;
    private ProgressBar progressBar;
    private EditText searchEdt;
    private ImageButton searchBtn;

    private FirebaseAuth firebaseAuth;

    private ActivityMainBinding binding;
    private ArrayList<SavedInfo> userBookArrayList;
    private SavedBookHistoryAdapter savedBookHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        getUser();
        checkUser();

        long timestamp = System.currentTimeMillis();
        String formatTime = MyApplication.formatTimestamp(timestamp);

        binding.idDateTv.setText(formatTime);

        initSavedBookHistory();
        initSavedForumHistory();

        binding.imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        binding.idAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BookSearchActivity.class));
            }
        });

    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void initSavedForumHistory() {
    }

    ArrayList<String> bookIds;
    private void initSavedBookHistory() {

        ArrayList<String> userBookIds = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child("books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    userBookIds.add(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("FavBooks");
        for (int i = 0; i < userBookIds.size(); i++){
            ref.child(userBookIds.get(i).toString())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userBookArrayList.clear();
                            for (DataSnapshot ds: snapshot.getChildren()){
                                SavedInfo savedInfo = ds.getValue(SavedInfo.class);
                                userBookArrayList.add(savedInfo);
                            }
                            savedBookHistoryAdapter = new SavedBookHistoryAdapter(userBookArrayList, MainActivity.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
                            binding.bookHistoryView.setLayoutManager(linearLayoutManager);

                            binding.bookHistoryView.setAdapter(savedBookHistoryAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        //SavedInfo savedInfo = new SavedInfo(title, publisher, publishedDate, pageCount, uid, noOfItems);
        //
        // //below line is use to pass our modal
        // //class in our array list.
        //savedInfoArrayList.add(savedInfo);
        //
        // //below line is use to pass our
        // //array list in adapter class.
        //BookAdapter adapter = new BookAdapter(bookInfoArrayList, MainActivity.this);
        //
        // //below line is use to add linear layout
        // //manager for our recycler view.
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        //RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.idRVBooks);
        //
        // //in below line we are setting layout manager and
        // //adapter to our recycler view.
        //mRecyclerView.setLayoutManager(linearLayoutManager);

    }

    private void getUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = ""+snapshot.child("name").getValue();
                        if (name != null) {
                            binding.idUserTv.setText(name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getBooksInfo(String query) {

        // creating a new array list.
        bookInfoArrayList = new ArrayList<>();

        // below line is use to initialize
        // the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        // below line is use to clear cache this
        // will be use when our data is being updated.
        mRequestQueue.getCache().clear();

        // below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

        // below line we are  creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);


        // below line is use to make json object request inside that we
        // are passing url, get method and getting json object. .
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressBar.setVisibility(View.GONE);
                binding.bookHistoryLoading.setVisibility(View.GONE);
                // inside on response method we are extracting all our json data.
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        String title = volumeObj.optString("title");
                        String subtitle = volumeObj.optString("subtitle");
                        JSONArray authorsArray = volumeObj.getJSONArray("authors");
                        String publisher = volumeObj.optString("publisher");
                        String publishedDate = volumeObj.optString("publishedDate");
                        String description = volumeObj.optString("description");
                        int pageCount = volumeObj.optInt("pageCount");
                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        String thumbnail = imageLinks.optString("thumbnail");
                        String previewLink = volumeObj.optString("previewLink");
                        String infoLink = volumeObj.optString("infoLink");
                        JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                        String buyLink = saleInfoObj.optString("buyLink");
                        String bookId = itemsObj.optString("id");
                        ArrayList<String> authorsArrayList = new ArrayList<>();
                        if (authorsArray.length() != 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authorsArrayList.add(authorsArray.optString(i));
                            }
                        }
                        // after extracting all the data we are
                        // saving this data in our modal class.
                        BookInfo bookInfo = new BookInfo(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink, bookId);

                        // below line is use to pass our modal
                        // class in our array list.
                        bookInfoArrayList.add(bookInfo);

                        // below line is use to pass our
                        // array list in adapter class.
                        BookAdapter adapter = new BookAdapter(bookInfoArrayList, MainActivity.this);

                        // below line is use to add linear layout
                        // manager for our recycler view.
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                        //RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.idRVBooks);

                        // in below line we are setting layout manager and
                        // adapter to our recycler view.
                        //mRecyclerView.setLayoutManager(linearLayoutManager);
                        binding.bookHistoryView.setLayoutManager(linearLayoutManager);
                        binding.bookHistoryView.setAdapter(adapter);
                        //mRecyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // displaying a toast message when we get any error from API
                    Toast.makeText(MainActivity.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // also displaying error message in toast.
                Toast.makeText(MainActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest);
    }

}