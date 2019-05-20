package com.appmoviles.andres.matchicesi;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appmoviles.andres.matchicesi.util.Util;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_CALLBACK_ID = 100;
    private static final int GALLERY_CALLBACK_ID = 101;

    private SpaceNavigationView spaceNavigationView;

    private UserFragment userFragment;
    private ConfigFragment configFragment;
    private MatchFragment matchFragment;
    private ChatFragment chatFragment;
    private NotificationsFragment notificationsFragment;

    private File photoFile;

    FirebaseAuth auth;
    FirebaseFirestore store;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        userFragment = new UserFragment();
        configFragment = new ConfigFragment();
        matchFragment = new MatchFragment();
        chatFragment = new ChatFragment();
        notificationsFragment = new NotificationsFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_content, matchFragment);
        transaction.commit();

        spaceNavigationView = findViewById(R.id.main_menu);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("PERFIL", R.drawable.profile_icon));
        spaceNavigationView.addSpaceItem(new SpaceItem("CHATS", R.drawable.chat_icon));
        spaceNavigationView.addSpaceItem(new SpaceItem("NOTIFICACIONES", R.drawable.notification_icon));
        spaceNavigationView.addSpaceItem(new SpaceItem("CONFIG", R.drawable.config_icon));

        spaceNavigationView.showIconOnly();

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
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
                    case "PERFIL":
                        transaction.replace(R.id.main_content, userFragment);
                        break;
                    case "CONFIG":
                        transaction.replace(R.id.main_content, configFragment);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        userFragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CALLBACK_ID && resultCode == RESULT_OK) {
            cropImage();
        }
        if (requestCode == GALLERY_CALLBACK_ID && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            photoFile = new File(Util.getPath(this, uri));
            cropImage();
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            StorageReference ref = storage.getReference().child("profiles").child(auth.getCurrentUser().getUid());

            try {
                FileInputStream fis = new FileInputStream(new File(Util.getPath(this, resultUri)));
                ref.putStream(fis).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        loadImage();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            //final Throwable cropError = UCrop.getError(data);
        }
    }

    private void cropImage() {
        Uri uri = FileProvider.getUriForFile(MainActivity.this, getPackageName(), photoFile);
        String destinationFileName = UUID.randomUUID().toString();

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);

        UCrop.Options options = new UCrop.Options();
        options.setToolbarWidgetColor(Color.parseColor("#E21662"));

        uCrop.withOptions(options);
        uCrop.start(MainActivity.this);
    }

    private void loadImage() {
        StorageReference ref = storage.getReference().child("profiles").child(auth.getCurrentUser().getUid());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                userFragment.setImage(uri.toString());

                store.collection("users").document(auth.getCurrentUser().getUid()).update("profilePic", uri.toString());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

}
