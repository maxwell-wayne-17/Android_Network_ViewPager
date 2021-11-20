package com.example.proj4_max_wayne;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int DEFAULT_MAX_IMGS = 15;
    private LayoutInflater li;
    private Context ctx;
    private int maxImgs;
    // Holds keys from petsAndImgs, will allow us to maintain consistent position
    private ArrayList<String> petNames;
    // Used to hold the link between pet names and their associated file name
    private HashMap<String, String> petsAndImgs;
    private ArrayList<Bitmap> pics; // Will most likely not use
    private DataVM myVm;

    public RecyclerViewAdapter(Context ctx, DataVM myVm, HashMap<String, String> petsAndImgs){
        this.ctx = ctx;
        li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.maxImgs = DEFAULT_MAX_IMGS;
        this.myVm = myVm;
        this.petsAndImgs = petsAndImgs;
        this.petNames = new ArrayList<>(petsAndImgs.keySet());
        maxImgs = (petNames.size() > 0) ? petNames.size() : 1;
    }

    // Might not need to use
    public void setPetsAndImgs(HashMap<String, String> petsAndImgs){ this.petsAndImgs = petsAndImgs; }

    // Might not need to use
    public void addBitmap(Bitmap pic){
        boolean newPic = true;
        for( Bitmap img : pics){
            if (img.sameAs(pic)){
                newPic = false;
            }
        }
        if (newPic){
            pics.add(pic);
        }
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {
        private static final int UNINITIALIZED = -1;
        private int position = UNINITIALIZED;
        ImageView iv;
        TextView tv;

        public ImgViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.image);
            tv = itemView.findViewById(R.id.imgName);
        }

        public void setPos(int position){ this.position = position; }

        public int getPos(){ return position; }

        public void setUi(Bitmap img, String name){
            iv.setImageBitmap(img);
            tv.setText(name);
        }
    }

    // Hold off these methods
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
        viewHolder.setPos(position);

        // Initialize the UI
        viewHolder.iv.setImageResource(R.drawable.hourglass);
        viewHolder.tv.setText("Initializing...");

        // Launch threads here to get image, should already have text
        // Get img info for thread
        String imgName = petNames.get(position);
        String imgFile = petsAndImgs.get(imgName);
        myVm.getImage(imgFile, viewHolder, imgName);

    }

    @Override
    public int getItemCount() {
        return this.maxImgs;
    }
}
