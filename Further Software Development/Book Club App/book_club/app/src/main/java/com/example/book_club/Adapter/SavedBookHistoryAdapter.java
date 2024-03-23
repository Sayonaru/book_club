package com.example.book_club.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_club.Domain.BookInfo;
import com.example.book_club.Domain.SavedInfo;
import com.example.book_club.R;

import java.util.ArrayList;

public class SavedBookHistoryAdapter extends RecyclerView.Adapter<SavedBookHistoryAdapter.viewholder> {

    int items;
    Context context;
    private ArrayList<SavedInfo> savedInfoArrayList;
    private ArrayList<BookInfo> bookInfoArrayList;

    public SavedBookHistoryAdapter(ArrayList<SavedInfo> savedInfoArrayList, Context context/*, ArrayList<BookInfo> bookInfoArrayList*/) {
        this.savedInfoArrayList = savedInfoArrayList;
        this.context = context;
        //this.bookInfoArrayList = bookInfoArrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_saved_book_history, parent, false);
        return new viewholder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedBookHistoryAdapter.viewholder holder, int position) {
        SavedInfo savedInfo = savedInfoArrayList.get(position);
        holder.titleTv.setText(savedInfo.getTitle());
        holder.tvPageCount.setText("No of Pages: "+savedInfo.getPageCount());
        holder.tvDate.setText(savedInfo.getPublishedDate());

        //holder.itemView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        // inside on click listener method we are calling a new activity
        //        // and passing all the data of that item in next intent.
        //        Intent i = new Intent(context, BookDetails.class);
        //        i.putExtra("title", bookInfo.getTitle());
        //        i.putExtra("subtitle", bookInfo.getSubtitle());
        //        i.putExtra("authors", bookInfo.getAuthors());
        //        i.putExtra("publisher", bookInfo.getPublisher());
        //        i.putExtra("publishedDate", bookInfo.getPublishedDate());
        //        i.putExtra("description", bookInfo.getDescription());
        //        i.putExtra("pageCount", bookInfo.getPageCount());
        //        i.putExtra("thumbnail", bookInfo.getThumbnail());
        //        i.putExtra("previewLink", bookInfo.getPreviewLink());
        //        i.putExtra("infoLink", bookInfo.getInfoLink());
        //        i.putExtra("buyLink", bookInfo.getBuyLink());
        //        i.putExtra("bookId", bookInfo.getBookId());
        //
        //        // after passing that data we are
        //        // starting our new intent.
        //        context.startActivity(i);
        //    }
        //});

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView titleTv, tvPageCount, tvDate;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.idTvTitle);
            tvPageCount = itemView.findViewById(R.id.idTvPageCount);
            tvDate = itemView.findViewById(R.id.idTvDate);
        }
    }
}
