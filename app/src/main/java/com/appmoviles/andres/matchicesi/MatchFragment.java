package com.appmoviles.andres.matchicesi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.appmoviles.andres.matchicesi.adapters.MatchAdapter;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

public class MatchFragment extends Fragment implements CardStackListener {

    DrawerLayout drawerLayout;
    private CardStackView cardStackView;
    CardStackLayoutManager manager;
    MatchAdapter adapter;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        cardStackView = view.findViewById(R.id.card_stack_view);

        adapter = new MatchAdapter();
        manager = new CardStackLayoutManager(getActivity(),this);

        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());


        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);


        cardStackView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        //Log.e(">>>", "onCardDragging: " + direction + " > " + manager.getTopPosition());
    }

    @Override
    public void onCardSwiped(Direction direction) {
        //Log.e(">>>", "onCardSwiped: " + direction + " > " + manager.getTopPosition());
        Log.e(">>>","Card "+adapter.getMatch(manager.getTopPosition()-1).getName()+" Direccion: "+direction);
    }

    @Override
    public void onCardRewound() {
        //Log.e(">>>", "onCardRewound: " + manager.getTopPosition());
    }

    @Override
    public void onCardCanceled() {
        Log.e(">>>", "onCardCanceled: " + manager.getTopPosition());
    }

    @Override
    public void onCardAppeared(View view, int position) {
        Log.e(">>>", "onCardDisappeared: > " + position);
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        Log.e(">>>", "onCardDisappeared: > " + position);
    }
}
