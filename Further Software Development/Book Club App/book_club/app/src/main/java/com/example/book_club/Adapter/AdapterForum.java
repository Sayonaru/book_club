package com.example.book_club.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_club.Domain.ModelForum;
import com.example.book_club.MyApplication;
import com.example.book_club.databinding.RowForumMessageBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterForum extends RecyclerView.Adapter<AdapterForum.HolderForum> {

    private Context context;
    private ArrayList<ModelForum> forumArrayList;
    private FirebaseAuth firebaseAuth;

    private RowForumMessageBinding binding;


    public AdapterForum(Context context, ArrayList<ModelForum> forumArrayList) {
        this.context = context;
        this.forumArrayList = forumArrayList;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderForum onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowForumMessageBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderForum(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderForum holder, int position) {
        ModelForum modelForum = forumArrayList.get(position);
        String id = modelForum.getId();
        String bookId = modelForum.getBookId();
        String message = modelForum.getMessage();
        String uid = modelForum.getUid();
        String timestamp = modelForum.getTimestamp();

        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));

        holder.dateTv.setText(date);
        holder.messageTv.setText(message);

        loadUserDetails(modelForum, holder);
    }

    private void loadUserDetails(ModelForum modelForum, HolderForum holder) {
        String uid = modelForum.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = ""+snapshot.child("name").getValue();

                        holder.nameTv.setText(name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return forumArrayList.size();
    }

    class HolderForum extends RecyclerView.ViewHolder {
        ShapeableImageView profileIv;
        TextView nameTv, dateTv, messageTv;

        public HolderForum(@NonNull View itemView) {
            super(itemView);

            profileIv = binding.profileIv;
            nameTv = binding.nameTv;
            dateTv = binding.dateTv;
            messageTv = binding.messageTv;
        }

    }
}
