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

import com.appmoviles.andres.matchicesi.adapters.MessagesAdapter;
import com.appmoviles.andres.matchicesi.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private String idMio;
    private String idAmigo;
    private String idChat;

    private MessagesAdapter messagesAdapter;
    private RecyclerView lista_mensajes;

    private EditText txt_mensaje;
    private Button btn_enviar_mensaje;

    private ArrayList<Message> lista;

    FirebaseAuth auth;
    FirebaseDatabase rtdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();

        idMio = auth.getCurrentUser().getUid();

        if (getIntent().hasExtra("id")) {
            idAmigo = getIntent().getStringExtra("id");
        } else {

        }

        lista_mensajes = findViewById(R.id.lista_chat);
        lista_mensajes.setLayoutManager(new LinearLayoutManager(this));

        btn_enviar_mensaje = findViewById(R.id.btn_enviar_mensaje);
        txt_mensaje = findViewById(R.id.txt_mensaje);
        lista = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(lista);
        lista_mensajes.setAdapter(messagesAdapter);
        lista_mensajes.setHasFixedSize(true);


        initChat();

    }

    private void initChat() {

        rtdb.getReference().child("chats").child(idMio).child(idAmigo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    String key = rtdb.getReference().child("chats").child(idMio).child(idAmigo).push().getKey();
                    rtdb.getReference().child("chats").child(idMio).child(idAmigo).setValue(key);
                    rtdb.getReference().child("chats").child(idAmigo).child(idMio).setValue(key);
                    idChat = key;
                    messagesAdapter = new MessagesAdapter(new ArrayList<Message>());

                } else {
                    idChat = dataSnapshot.getValue(String.class);

                }
                cargarMensajes();
                activeListenerBottom();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cargarMensajes() {

        rtdb.getReference().child("mensajes").child(idChat).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                lista.add(dataSnapshot.getValue(Message.class));
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

    private void activeListenerBottom() {
        lista_mensajes.setAdapter(messagesAdapter);
        lista_mensajes.setHasFixedSize(true);


        btn_enviar_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = txt_mensaje.getText().toString();
                Message men = new Message();
                men.setText(mensaje);
                men.setId(idMio);
                rtdb.getReference().child("mensajes").child(idChat).push().setValue(men);
                txt_mensaje.setText("");

            }
        });
    }

}
