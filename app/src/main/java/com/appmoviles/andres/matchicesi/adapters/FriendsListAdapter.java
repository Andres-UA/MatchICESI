package com.appmoviles.andres.matchicesi.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.CustomViewHolder> {

    ArrayList<Friendship> data;
    FirebaseFirestore storage;
    FirebaseAuth auth;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;

        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public void showAllFriends(ArrayList<Friendship> allFriends) {
        for (int i = 0; i < allFriends.size(); i++) {
            if (!data.contains(allFriends.get(i))) data.add(allFriends.get(i));
        }
        notifyDataSetChanged();
    }

    public void addFriend(Friendship friend) {
        data.add(friend);
        notifyDataSetChanged();
    }

    public FriendsListAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item_list_view, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        storage = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        String id = data.get(position).getSender();

        if (auth.getCurrentUser().getUid().equals(data.get(position).getSender())) {
            id = data.get(position).getReceiver();
        }

        storage.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ((TextView) holder.root.findViewById(R.id.friend_item_list_name)).setText(document.get("names").toString());

                        Glide.with(holder.root.getContext()).load(document.get("profilePic").toString()).into((ImageView) holder.root.findViewById(R.id.img_friend));
                    }
                }
            }
        });

        ((TextView) holder.root.findViewById(R.id.friend_item_list_message)).setText("Ultimo mensaje");

        final String friendId = id;
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.root.getContext(), ChatActivity.class);
                intent.putExtra("id", friendId);
                holder.root.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
