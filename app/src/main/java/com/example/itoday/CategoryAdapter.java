package com.example.itoday;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    ArrayList<String> titles;
    ArrayList<Integer> images;
    private OnItemClickListener onItemClickListener;

    public CategoryAdapter(ArrayList<String> titles,ArrayList<Integer> images)
    {
        this.titles = titles;
        this.images = images;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card,parent,false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(v,onItemClickListener);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String title = titles.get(position);
        Integer img = images.get(position);

        holder.categoryText.setText(title);
        holder.categoryImg.setImageResource(img);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        public ImageView categoryImg;
        public TextView categoryText;

        public CategoryViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            categoryImg = itemView.findViewById(R.id.card_logo);
            categoryText = itemView.findViewById(R.id.card_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            onItemClickListener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
}
