package com.example.book_club.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_club.Domain.ModelComment;
import com.example.book_club.MyApplication;
import com.example.book_club.databinding.RowCommentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.HolderComment> {

    private Context context;
    private ArrayList<ModelComment> commentArrayList;
    private FirebaseAuth firebaseAuth;

    //view binding
    private RowCommentBinding binding;

    //Constructor
    public AdapterComment(Context context, ArrayList<ModelComment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = RowCommentBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderComment(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderComment holder, int position) {
        ModelComment modelComment = commentArrayList.get(position);
        String id = modelComment.getId();
        String bookId = modelComment.getBookId();
        String comment = modelComment.getComment();
        String uid = modelComment.getUid();
        String timestamp = modelComment.getTimestamp();

        //Format date
        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));

        //set data
        holder.dateTv.setText(date);
        holder.commentTv.setText(comment);

        loadUserDetails(modelComment, holder);

        //handle click show option to delete comment
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid.equals(firebaseAuth.getUid())){
                    deleteComment(modelComment, holder);
                }
            }
        });
    }

    private void deleteComment(ModelComment modelComment, HolderComment holder) {
        //show confirm dialog before deleting comment
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Comment")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete from dialog clicked, begin delete
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FavBooks");
                        ref.child(modelComment.getBookId()).child("Comments").child(modelComment.getId())
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Successfully deleted review", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to delete due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel clicked
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void loadUserDetails(ModelComment modelComment, HolderComment holder) {
        String uid = modelComment.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get name
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
        return commentArrayList.size(); //return records size, number of records
    }

    //view holder class for row_comment
    class HolderComment extends RecyclerView.ViewHolder {

        //ui views of row_comment
        ShapeableImageView profileIv;
        TextView nameTv, dateTv, commentTv;

        public HolderComment(@NonNull View itemView) {
            super(itemView);

            profileIv = binding.profileIv;
            nameTv = binding.nameTv;
            dateTv = binding.dateTv;
            commentTv = binding.commentTv;
        }
    }
}
