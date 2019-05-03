package com.appmoviles.andres.matchicesi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmoviles.andres.matchicesi.R;
import com.appmoviles.andres.matchicesi.model.Friend;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.CustomViewHolder> {

    ArrayList<Friend> data;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;

        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public void showAllFriends(ArrayList<Friend> allFriends) {
        for (int i = 0; i < allFriends.size(); i++) {
            if (!data.contains(allFriends.get(i))) data.add(allFriends.get(i));
        }
        notifyDataSetChanged();
    }

    public void addFriend(Friend friend) {
        data.add(friend);
        notifyDataSetChanged();
    }

    public FriendsListAdapter() {
        data = new ArrayList<>();
        data.add(new Friend("ID", "Amigo de prueba"));
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item_list_view, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        ((TextView) holder.root.findViewById(R.id.friend_item_list_name)).setText(data.get(position).getName());
        ((TextView) holder.root.findViewById(R.id.friend_item_list_message)).setText("Ultimo mensaje");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
