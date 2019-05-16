package com.appmoviles.andres.matchicesi;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appmoviles.andres.matchicesi.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_CALLBACK_ID = 100;
    private static final int GALLERY_CALLBACK_ID = 101;
    private File photoFile;

    private MaterialButton btnLogout;
    private SpaceNavigationView spaceNavigationView;

    private UserFragment userFragment;
    private HomeFragment homeFragment;
    private MatchFragment matchFragment;
    private ChatFragment chatFragment;
    private NotificationsFragment notificationsFragment;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        userFragment = new UserFragment();
        homeFragment = new HomeFragment();
        matchFragment = new MatchFragment();
        chatFragment = new ChatFragment();
        notificationsFragment = new NotificationsFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_content, homeFragment);
        transaction.commit();

        spaceNavigationView = findViewById(R.id.main_menu);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("INICIO", R.drawable.home_icon));
        spaceNavigationView.addSpaceItem(new SpaceItem("CHATS", R.drawable.chat_icon));
        spaceNavigationView.addSpaceItem(new SpaceItem("NOTIFICACIONES", R.drawable.notification_icon));
        spaceNavigationView.addSpaceItem(new SpaceItem("PERFIL", R.drawable.profile_icon));

        FragmentManager manager1 = getSupportFragmentManager();
        FragmentTransaction transaction1 = manager1.beginTransaction();
        transaction1.replace(R.id.main_content, matchFragment);
        transaction1.commit();

        spaceNavigationView.showIconOnly();

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {


            @Override
            public void onCentreButtonClick() {
                //Toast.makeText(MainActivity.this, "onCentreButtonClick", Toast.LENGTH_SHORT).show();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content, matchFragment);
                transaction.commit();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                //Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                switch (itemName) {
                    case "INICIO":
                        transaction.replace(R.id.main_content, homeFragment);
                        break;
                    case "PERFIL":
                        transaction.replace(R.id.main_content, userFragment);
                        break;
                    case "NOTIFICACIONES":
                        transaction.replace(R.id.main_content, notificationsFragment);
                        break;
                    case "CHATS":
                        transaction.replace(R.id.main_content, chatFragment);
                        break;
                }
                //transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                //Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });

       /* btnLogout = findViewById(R.id.btn_signout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });*/

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Luego de tomar la foto y guardarla
        if (requestCode == CAMERA_CALLBACK_ID && resultCode == RESULT_OK) {
            //subirImagen();
        }
        if (requestCode == GALLERY_CALLBACK_ID && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            photoFile = new File(Util.getPath(this, uri));
            //subirImagen();
        }
    }

}
