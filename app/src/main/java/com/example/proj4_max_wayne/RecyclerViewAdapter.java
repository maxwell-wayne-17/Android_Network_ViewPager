package com.example.proj4_max_wayne;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int DEFAULT_MAX_IMGS = 15;
    private LayoutInflater li;
    private Context ctx;
    private int maxImgs;

    public RecyclerViewAdapter(Context ctx){
        this(ctx, DEFAULT_MAX_IMGS);
    }

    public RecyclerViewAdapter(Context ctx, int maxImgs){
        this.ctx = ctx;
        li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.maxImgs = maxImgs;
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {
        private static final int UNINITIALIZED = -1;
        int num = UNINITIALIZED;
        ImageView iv;
        TextView tv;

        public ImgViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.image);
            tv = itemView.findViewById(R.id.imgName);
        }

        public void setNum(int num){ this.num = num; }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Call this when we need to create a brand new PagerViewHolder
        View view = li.inflate(R.layout.img_layout, parent, false);
        return new ImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Passing in an existing instance, reuse the internal resources
        // Pass our data to our ViewHolder
        ImgViewHolder viewHolder = (ImgViewHolder) holder;
        viewHolder.setNum(position);

        // Initialize the UI
        viewHolder.iv.setImageResource(R.drawable.hourglass);
        viewHolder.tv.setText("Initializing...");

        // Launch threads here to get image and text

    }

    @Override
    public int getItemCount() {
        return this.maxImgs;
    }
}
