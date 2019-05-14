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

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    ArrayList<Match> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView root;

        public ViewHolder(CardView v) {
            super(v);
            root = v;
        }
    }

    public void addFriend(Match match) {
        data.add(match);
        notifyDataSetChanged();
    }

    public MatchAdapter() {
        data = new ArrayList<>();
        data.add(new Match("GOW", "https://cdn.vox-cdn.com/thumbor/W_Je8ktsOWTbbdINLNMOalM7TwA=/0x0:1100x754/1200x675/filters:focal(462x289:638x465)/cdn.vox-cdn.com/uploads/chorus_image/image/59383015/GoW_A_art.0.jpg", "18"));
        data.add(new Match("Horizon", "https://cdn.hobbyconsolas.com/sites/navi.axelspringer.es/public/styles/main_element/public/media/image/2016/10/horizon-zero-dawn-nuevas-imagenes_0.jpg", "20"));
        data.add(new Match("Maincra", "https://http2.mlstatic.com/minecraft-windows-10-edition-D_NQ_NP_800950-MLM29102936573_012019-F.jpg", "18"));
        data.add(new Match("El Rengo", "https://cdn.hobbyconsolas.com/sites/navi.axelspringer.es/public/styles/main_element/public/media/image/2017/05/fondo-pantalla-league-legends_4.jpg", "18"));
        data.add(new Match("Zoe", "https://i2.wp.com/hablamosdegamers.com/wp-content/uploads/2019/03/League-of-Legends-Campeones-mejorados-y-nerfeados-para-el-Parche-9.6.jpg", "19"));

        notifyDataSetChanged();
    }

    public Match getMatch(int index) {
        return data.get(index);
    }

    public void setMatches(ArrayList<Match> matches) {
        data = matches;
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
        ((TextView) holder.root.findViewById(R.id.match_item_age)).setText("18");
        Glide.with(holder.root.findViewById(R.id.item_image)).load(data.get(position).getImage()).into((ImageView) holder.root.findViewById(R.id.item_image));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
