package com.example.itoday;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    ArrayList<NewsData> newsData;
    String count = "1";
    Context context;

    public NewsAdapter(ArrayList<NewsData> newsData,Context context)
    {
        this.newsData = newsData;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card,parent,false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(v);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        final NewsData n = newsData.get(position);
        holder.newsTitle.setText(n.getTitle());
        holder.newsAuthor.setText("By " + n.getAuthor());
        holder.newsDescription.setText(n.getDescription());
        Picasso.get().load(n.getUrl_image()).into(holder.newsImageUrl);
        //holder.newsImageUrl.setImageResource(R.drawable.image1);
        holder.newsUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CLICKED :",n.getTitle());
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(n.getUrl()));
                context.startActivity(i);
            }
        });
        holder.newsBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String key = firebaseAuth.getCurrentUser().getUid();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(key);
                databaseReference.child("News Article" + count).setValue(n);
                int c = Integer.parseInt(count) + 1;
                count = String.valueOf(c);
                Toast.makeText(context,"Added to Bookmark", Toast.LENGTH_LONG).show();
                Log.e("MESSAGE :","BOOKMARKED");
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsData.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        ImageView newsImageUrl;
        TextView newsTitle,newsAuthor,newsDescription;
        Button newsUrl;
        ImageButton newsBookmark;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            newsImageUrl = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsAuthor = itemView.findViewById(R.id.news_author);
            newsDescription = itemView.findViewById(R.id.news_desc);
            newsUrl = itemView.findViewById(R.id.news_url);
            newsBookmark = itemView.findViewById(R.id.news_bookmark);
        }
    }
}
