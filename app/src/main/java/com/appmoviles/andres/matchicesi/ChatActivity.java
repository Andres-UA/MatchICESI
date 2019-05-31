package com.appmoviles.andres.matchicesi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmoviles.andres.matchicesi.adapters.MessagesAdapter;
import com.appmoviles.andres.matchicesi.database.DBHandler;
import com.appmoviles.andres.matchicesi.model.Message;
import com.appmoviles.andres.matchicesi.service.NotificationService;
import com.appmoviles.andres.matchicesi.util.Network;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private String myId;
    private String friendId;
    private String chatId;
    private String friendName;
    private String myName;

    private MessagesAdapter messagesAdapter;
    private RecyclerView messagesList;
    private EditText etMessage;
    private Button btnSendMessage;
    private ImageView btnGoBack;
    private TextView tvFriendName;

    private ArrayList<Message> list;

    FirebaseAuth auth;
    FirebaseDatabase rtdb;
    FirebaseFirestore store;
    DBHandler localdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();
        store = FirebaseFirestore.getInstance();
        localdb = DBHandler.getInstance(this);

        tvFriendName = findViewById(R.id.tv_friend_name);

        btnGoBack = findViewById(R.id.btn_go_back);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myId = auth.getCurrentUser().getUid();

        if (getIntent().hasExtra("id")) {
            friendId = getIntent().getStringExtra("id");
        }

        store.collection("users").document(friendId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        friendName = document.get("names").toString();
                        tvFriendName.setText(friendName);
                    }
                }
            }
        });

        store.collection("users").document(myId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        myName = document.get("names").toString();
                    }
                }
            }
        });

        messagesList = findViewById(R.id.lista_chat);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setReverseLayout(true);
        messagesList.setLayoutManager(layout);

        btnSendMessage = findViewById(R.id.btn_enviar_mensaje);
        etMessage = findViewById(R.id.txt_mensaje);

        list = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(list);

//        if (Network.isOnlineNet()) {
//            initialize();
//            Log.e(">>>","ONLINE MODE");
//        } else {
//            Log.e(">>>","OFFLINE MODE");
//            initializeOffline();
//        }
        initialize();
    }

    private void initializeOffline() {
        ArrayList<Message> messages = localdb.getAllMensajesUserWithIdChat(chatId);
        for (Message message : messages) {
            Log.e(">>>", "Mensaje: " + message.getText());
            list.add(0, message);
            messagesAdapter.notifyDataSetChanged();
        }
    }

    private void initialize() {

        rtdb.getReference().child("chats").child(myId).child(friendId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    String key = rtdb.getReference().child("chats").child(myId).child(friendId).push().getKey();
                    rtdb.getReference().child("chats").child(myId).child(friendId).setValue(key);
                    rtdb.getReference().child("chats").child(friendId).child(myId).setValue(key);
                    chatId = key;
                } else {
                    chatId = dataSnapshot.getValue(String.class);

                }
                loadMessages();
                activeSendButton();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMessages() {
        localdb.deleteMessagesChat(chatId);
        rtdb.getReference().child("messages").child(chatId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                list.add(0, message);
                localdb.createMensaje(message, chatId);
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void activeSendButton() {
        messagesList.setAdapter(messagesAdapter);
        messagesList.setHasFixedSize(true);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etMessage.getText().toString();
                Message message = new Message();
                message.setText(text);
                message.setId(myId);
                message.setName(myName);
                rtdb.getReference().child("messages").child(chatId).push().setValue(message);
                etMessage.setText("");
            }
        });
    }

}
