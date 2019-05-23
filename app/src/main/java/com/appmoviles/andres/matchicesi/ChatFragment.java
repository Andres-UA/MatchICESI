package com.appmoviles.andres.matchicesi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appmoviles.andres.matchicesi.adapters.FriendsListAdapter;
import com.appmoviles.andres.matchicesi.model.Friendship;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatFragment extends Fragment {

    private RecyclerView rvFriendsList;
    private FriendsListAdapter friendsAdapter;

    FirebaseFirestore store;
    FirebaseAuth auth;

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

        store = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        rvFriendsList = view.findViewById(R.id.rv_friends_list);
        friendsAdapter = new FriendsListAdapter();

        rvFriendsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFriendsList.setAdapter(friendsAdapter);
        rvFriendsList.setHasFixedSize(true);

        store.collection("friendship").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Friendship friendship = document.toObject(Friendship.class);
                                if (friendship.getReceiver().equals(auth.getCurrentUser().getUid()) || friendship.getSender().equals(auth.getCurrentUser().getUid())) {
                                    if (friendship.getState().equals("FRIENDSHIP")) {
                                        friendsAdapter.addFriend(friendship);
                                    }
                                }
                            }
                        }
                    }
                });

        return view;
    }


}
