package com.appmoviles.andres.matchicesi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.appmoviles.andres.matchicesi.adapters.MatchAdapter;
import com.appmoviles.andres.matchicesi.model.Friend;
import com.appmoviles.andres.matchicesi.model.Friendship;
import com.appmoviles.andres.matchicesi.model.Match;
import com.appmoviles.andres.matchicesi.model.MatchData;
import com.appmoviles.andres.matchicesi.model.Request;
import com.appmoviles.andres.matchicesi.model.User;
import com.appmoviles.andres.matchicesi.model.UserData;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MatchFragment extends Fragment implements CardStackListener {

    private CardStackView cardStackView;
    CardStackLayoutManager manager;
    MatchAdapter adapter;

    FirebaseAuth auth;
    FirebaseFirestore store;

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
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        auth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();

        //getMatches();


        cardStackView = view.findViewById(R.id.card_stack_view);

        adapter = new MatchAdapter();
        manager = new CardStackLayoutManager(getActivity(), this);

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
        doMatches();
        return view;
    }

    public int age(Calendar birthDate) {
        Calendar actualDate = Calendar.getInstance();
        int years = actualDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        int months = actualDate.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH);
        int days = actualDate.get(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH);

        if (months < 0 || (months == 0 && days < 0)) {
            years--;
        }
        return years;
    }

    private void getMatches() {
        final ArrayList<Match> matches = new ArrayList<>();
        store.collection("matches").whereEqualTo("user_one", auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        final String user_two_id = document.get("user_two").toString();
                        store.collection("users").document(user_two_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        User user = document.toObject(User.class);
                                        GregorianCalendar calendar = new GregorianCalendar();
                                        calendar.setTime(user.getBirthDate());
                                        int age = age(calendar);

                                        Match match = new Match(user.getId(), user.getNames(), user.getProfilePic(), age);
                                        matches.add(match);
                                    }
                                }
                                adapter.setMatches(matches);
                            }
                        });
                    }
                } else {
                    Log.e(">>>", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void doMatches() {
        store.collection("user_data").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        final UserData me = document.toObject(UserData.class);
                        final ArrayList<Friendship> friendships = new ArrayList<>();

                        store.collection("friendship").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Friendship friendship = document.toObject(Friendship.class);
                                        friendships.add(friendship);
                                    }

                                    store.collection("user_data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    UserData userData = document.toObject(UserData.class);

                                                    MatchData matchData = match(me, userData, friendships);
                                                    if (matchData != null) {
                                                        store.collection("users").document(matchData.getUser()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot document = task.getResult();
                                                                    if (document.exists()) {
                                                                        User user = document.toObject(User.class);

                                                                        GregorianCalendar calendar = new GregorianCalendar();
                                                                        calendar.setTime(user.getBirthDate());
                                                                        int age = age(calendar);

                                                                        Match match = new Match(user.getId(), user.getNames(), user.getProfilePic(), age);
                                                                        adapter.addMatch(match);
                                                                    }
                                                                }

                                                            }
                                                        });
                                                    }
                                                }
                                            } else {
                                                Log.e(">>>", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                                } else {
                                    Log.e(">>>", "Error getting documents: ", task.getException());
                                }
                            }
                        });

                    }
                }
            }
        });
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        //Log.e(">>>", "onCardDragging: " + direction + " > " + manager.getTopPosition());
    }

    @Override
    public void onCardSwiped(Direction direction) {
        String receiver = adapter.getMatch(manager.getTopPosition() - 1).getId();
        String state = state(direction);
        final Friendship friendship = new Friendship(auth.getCurrentUser().getUid(), receiver, state, new Date());
        store.collection("friendship").add(friendship).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                friendship.setId(documentReference.getId());
                store.collection("friendship").document(friendship.getId()).update("id", friendship.getId());
            }
        });
    }

    @Override
    public void onCardRewound() {
        //Log.e(">>>", "onCardRewound: " + manager.getTopPosition());
    }

    @Override
    public void onCardCanceled() {
        Log.e(">>>", "onCardCanceled: " + manager.getTopPosition());
    }

    public String state(Direction direction) {
        String res = "CANCEL";
        if (direction.equals(Direction.Left)) {
            res = "CANCEL";
        }
        if (direction.equals(Direction.Right)) {
            res = "REQUEST";
        }
        return res;
    }

    @Override
    public void onCardAppeared(View view, int position) {
        //Log.e(">>>", "onCardDisappeared: > " + position);
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        //Log.e(">>>", "onCardDisappeared: > " + position);
    }

    private MatchData match(UserData me, UserData user, ArrayList<Friendship> friendships) {
        MatchData matchData = null;
        if (!me.getId().equals(user.getId())) {
            Log.e(">>>", "TRUE => USER1-> " + me.getId() + " VS USER2-> " + user.getId());
            if (checkFriendship(friendships, me.getId(), user.getId()) == false) {
                double identity = arrayMatch(me.getDescriptors(), user.getDescriptors());
                double movie = arrayMatch(me.getMovies(), user.getMovies());
                double music = arrayMatch(me.getMusics(), user.getMusics());
                double book = arrayMatch(me.getBooks(), user.getBooks());
                double fun = exactMatch(me.getFuns(), user.getFuns());

                double music_value = score(me.getMusicRank(), user.getMusicRank());
                double movie_value = score(me.getMovieRank(), user.getMovieRank());
                double book_value = score(me.getBookRank(), user.getBookRank());

                double[] percents = percent(movie_value, book_value, music_value);

                double final_identity = identity * 0.3;
                double final_movie = movie * percents[0];
                double final_music = music * percents[1];
                double final_book = book * percents[2];
                double final_fun = fun * 0.2;

                double global = final_identity + final_movie + final_music + final_book + final_fun;

                matchData = new MatchData(user.getId(), fix(movie, 2), fix(book, 2), fix(music, 2), fix(fun, 2), fix(identity, 2), fix(global, 2));

            }
        }
        return matchData;
    }

    private boolean checkFriendship(ArrayList<Friendship> friends, String userId1, String userId2) {
        boolean res = false;
        for (Friendship friendship : friends) {
            if ((friendship.getSender().equals(userId1) && friendship.getReceiver().equals(userId2)) || (friendship.getSender().equals(userId2) && friendship.getReceiver().equals(userId1))) {
                res = true;
                break;
            }
        }
        return res;
    }

    private double exactMatch(ArrayList<String> me, ArrayList<String> user) {
        int count = 0;

        if (me.get(0).equals(user.get(0))) {
            count += 1;
        }

        if (me.get(1).equals(user.get(1))) {
            count += 1;
        }

        if (me.get(2).equals(user.get(2))) {
            count += 1;
        }

        if (me.get(3).equals(user.get(3))) {
            count += 1;
        }

        double total = (double) count;

        double percent = count * 100 / (double) 4;
        return fix(percent, 2);
    }

    public double score(int user1_score, int user2_score) {
        int min = Math.min(user1_score, user2_score);
        int max = Math.max(user1_score, user2_score);
        return (double) (min / max);
    }

    private double[] percent(double movie, double books, double music) {
        double total = movie + books + music;
        double percent1 = (movie / total);
        double percent2 = (books / total);
        double percent3 = (music / total);

        double[] res = {fix(percent1 / 2, 2), fix(percent2 / 2, 2), fix(percent3 / 2, 2)};

        return res;
    }

    private double arrayMatch(ArrayList<String> me, ArrayList<String> user) {
        int count = 0;
        for (String item : me) {
            for (String item2 : user) {
                if (item.equals(item2)) {
                    count += 1;
                }
            }
        }

        double percent = count * 100 / (double) me.size();
        return fix(percent, 2);
    }

    public double fix(double initial, int points) {
        double parteEntera, resultado;
        resultado = initial;
        parteEntera = Math.floor(resultado);
        resultado = (resultado - parteEntera) * Math.pow(10, points);
        resultado = Math.round(resultado);
        resultado = (resultado / Math.pow(10, points)) + parteEntera;
        return resultado;
    }


}
