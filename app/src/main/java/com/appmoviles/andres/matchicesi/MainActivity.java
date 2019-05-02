package com.appmoviles.andres.matchicesi;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnLogout;
    private SpaceNavigationView spaceNavigationView;

    private UserFragment userFragment;
    private HomeFragment homeFragment;
    private MatchFragment matchFragment;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        userFragment = new UserFragment();
        homeFragment = new HomeFragment();
        matchFragment = new MatchFragment();

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
                }
                //transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
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

}
