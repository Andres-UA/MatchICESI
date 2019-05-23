package com.appmoviles.andres.matchicesi.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmoviles.andres.matchicesi.ChatActivity;
import com.appmoviles.andres.matchicesi.R;
import com.appmoviles.andres.matchicesi.model.Friendship;
import com.appmoviles.andres.matchicesi.model.Photo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.CustomViewHolder> {

    ArrayList<Photo> data;
    FirebaseFirestore storage;
    FirebaseAuth auth;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public CardView root;

        public CustomViewHolder(CardView v) {
            super(v);
            root = v;
        }
    }

    public void addPhoto(Photo photo) {
        data.add(0, photo);
        notifyDataSetChanged();
    }

    public GalleryAdapter() {
        data = new ArrayList<>();
    }

    public GalleryAdapter(ArrayList<Photo> photos) {
        data = photos;
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item_list_view, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        ImageView imageView = holder.root.findViewById(R.id.item_gallery);
        Glide.with(holder.root.getContext()).load(data.get(position).getUrl()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}