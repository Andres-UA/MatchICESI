package com.appmoviles.andres.matchicesi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.appmoviles.andres.matchicesi.adapters.FriendsListAdapter;
import com.appmoviles.andres.matchicesi.adapters.RequestAdapter;
import com.appmoviles.andres.matchicesi.model.Friendship;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NotificationsFragment extends Fragment {

    private RecyclerView rvRequestList;
    private RequestAdapter requestAdapter;

    private RelativeLayout layout;
    private ScrollView layoutNotifications;

    FirebaseFirestore store;
    FirebaseAuth auth;

    public NotificationsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);


        store = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        layout = view.findViewById(R.id.layout_not_notification);
        layoutNotifications = view.findViewById(R.id.layout_notification);

        rvRequestList = view.findViewById(R.id.rv_request_list);
        requestAdapter = new RequestAdapter();

        rvRequestList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRequestList.setAdapter(requestAdapter);
        rvRequestList.setHasFixedSize(true);

        store.collection("friendship")
                .whereEqualTo("receiver", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean empty = true;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Friendship friendship = document.toObject(Friendship.class);
                                if (friendship.getState().equals("REQUEST")) {
                                    requestAdapter.addRequest(friendship);
                                    empty = false;
                                }
                            }
                            if (empty) {
                                layoutNotifications.setVisibility(View.INVISIBLE);
                                layout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

        return view;
    }


}
