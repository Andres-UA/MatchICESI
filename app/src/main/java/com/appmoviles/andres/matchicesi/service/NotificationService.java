package com.appmoviles.andres.matchicesi.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmoviles.andres.matchicesi.MainActivity;
import com.appmoviles.andres.matchicesi.R;
import com.appmoviles.andres.matchicesi.model.Friendship;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class NotificationService extends Service {

    public static final String CHANNEL_ID = "MatchICESI";
    public static final String CHANNEL_NAME = "MatchICESI";
    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    private NotificationManager manager;

    FirebaseFirestore store;
    FirebaseAuth auth;

    public NotificationService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        store = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        friendshipNotifications();
    }

    private void friendshipNotifications() {
        store.collection("friendship").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(">>>", "Listen failed.", e);
                    return;
                }

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean notifications = sharedPreferences.getBoolean("notification", true);

                if (notifications) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Friendship friendship = doc.toObject(Friendship.class);
                        if (friendship.getReceiver().equals(auth.getCurrentUser().getUid()) || friendship.getSender().equals(auth.getCurrentUser().getUid())) {
                            if (friendship.getState().equals("REQUEST")) {
//
//                            String id = friendship.getSender();
//
//                            if (auth.getCurrentUser().getUid().equals(friendship.getSender())) {
//                                id = friendship.getReceiver();
//                            }
//
                                if (auth.getCurrentUser().getUid().equals(friendship.getReceiver())) {
                                    store.collection("users").document(friendship.getSender()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    showNotification(document.get("names").toString());
                                                }
                                            }
                                        }
                                    });
                                }


                            }
                        }
                    }
                }


            }
        });
    }

    private boolean checkConection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(">>>", "Servicio iniciado...");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(">>>", "Servicio destruido...");
    }

    public void showNotification(String name) {

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
            manager.createNotificationChannel(canal);
        }

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle("Nueva solicitud")
                .setContentText(name + " quiere ser tu amigo")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.logo))
                .setDefaults(Notification.DEFAULT_ALL)
                .setColor(Color.parseColor("#2b0010"))
                .setAutoCancel(true)
                .setVibrate(new long[]{100, 250, 100, 500})
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        manager.notify(1, builder.build());

    }

    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
