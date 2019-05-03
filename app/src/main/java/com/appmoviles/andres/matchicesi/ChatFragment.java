package com.appmoviles.andres.matchicesi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appmoviles.andres.matchicesi.adapters.FriendsListAdapter;
import com.appmoviles.andres.matchicesi.model.Friend;

public class ChatFragment extends Fragment {

    private RecyclerView rvFriendsList;
    private FriendsListAdapter friendsAdapter;

    public ChatFragment() {
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
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        rvFriendsList = view.findViewById(R.id.rv_friends_list);
        friendsAdapter = new FriendsListAdapter();

        rvFriendsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFriendsList.setAdapter(friendsAdapter);
        rvFriendsList.setHasFixedSize(true);

        friendsAdapter.addFriend(new Friend("ID","Amigo de prueba"));

        return view;
    }


}
