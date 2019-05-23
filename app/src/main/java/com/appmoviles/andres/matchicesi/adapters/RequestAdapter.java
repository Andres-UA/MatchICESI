package com.appmoviles.andres.matchicesi.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmoviles.andres.matchicesi.R;
import com.appmoviles.andres.matchicesi.model.Friendship;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.CustomViewHolder> {

    ArrayList<Friendship> data;
    FirebaseFirestore storage;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;

        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public void addRequest(Friendship request) {
        data.add(request);
        notifyDataSetChanged();
    }

    public RequestAdapter() {
        data = new ArrayList<>();
    }

    public void setData(ArrayList<Friendship> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item_list_view, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        storage = FirebaseFirestore.getInstance();

        storage.collection("users").document(data.get(position).getSender()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ((TextView) holder.root.findViewById(R.id.request_item_list_name)).setText(document.get("names").toString());
                    }
                }
            }
        });

        holder.root.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.collection("friendship").document(data.get(position).getId()).update("state", "FRIENDSHIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        data.remove(data.get(position));
                        notifyDataSetChanged();
                    }
                });
            }
        });

        holder.root.findViewById(R.id.btn_decline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.collection("friendship").document(data.get(position).getId()).update("state", "CANCEL").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        data.remove(data.get(position));
                        notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}