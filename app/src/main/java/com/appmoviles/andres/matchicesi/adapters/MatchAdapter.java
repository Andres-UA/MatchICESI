package com.appmoviles.andres.matchicesi.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmoviles.andres.matchicesi.R;
import com.appmoviles.andres.matchicesi.model.Match;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    ArrayList<Match> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView root;

        public ViewHolder(CardView v) {
            super(v);
            root = v;
        }
    }

    public void addMatch(Match match) {
        data.add(match);
        notifyDataSetChanged();
    }

    public MatchAdapter() {
        data = new ArrayList<>();
    }

    public Match getMatch(int index) {
        return data.get(index);
    }

    public void setMatches(ArrayList<Match> matches) {
        data = matches;
        notifyDataSetChanged();
    }

    public ArrayList<Match> getMatches() {
        return data;
    }

    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.match_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ((TextView) holder.root.findViewById(R.id.match_item_name)).setText(data.get(position).getName());
        ((TextView) holder.root.findViewById(R.id.match_item_age)).setText("Descripción...");
        Glide.with(holder.root.findViewById(R.id.item_image)).load(data.get(position).getImage()).into((ImageView) holder.root.findViewById(R.id.item_image));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
