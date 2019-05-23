package com.appmoviles.andres.matchicesi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmoviles.andres.matchicesi.adapters.FriendsListAdapter;
import com.appmoviles.andres.matchicesi.adapters.GalleryAdapter;
import com.appmoviles.andres.matchicesi.model.Match;
import com.appmoviles.andres.matchicesi.model.Photo;
import com.appmoviles.andres.matchicesi.model.User;
import com.appmoviles.andres.matchicesi.util.DateUtils;
import com.appmoviles.andres.matchicesi.util.Util;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class UserFragment extends Fragment {

    private static final int CAMERA_CALLBACK_ID = 100;
    private static final int GALLERY_CALLBACK_ID = 101;
    private static final int GALLERY_ADD_CALLBACK_ID = 102;

    private ImageView img_principal;
    private File photoFile;
    private TextView edit_nom_edad;
    private ArrayList<ImageView> imagenes;
    private Button btnAddPhoto;
    private TextView edit_sobre_ti;

    private ImageView btnCamera;
    private ImageView btnGallery;

    private RecyclerView rvGallery;
    private GalleryAdapter galleryAdapter;

    FirebaseAuth auth;
    FirebaseFirestore store;
    FirebaseStorage storage;

    public UserFragment() {
    }

    public void setImage(String uri) {
        Glide.with(getActivity()).load(uri).into(img_principal);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user, container, false);

        auth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        rvGallery = view.findViewById(R.id.rv_gallery);
        galleryAdapter = new GalleryAdapter();

        rvGallery.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvGallery.setAdapter(galleryAdapter);
        rvGallery.setHasFixedSize(true);

        btnCamera = view.findViewById(R.id.btn_camera);
        btnGallery = view.findViewById(R.id.btn_gallery);

        edit_sobre_ti = view.findViewById(R.id.edit_sobre_ti);
        edit_nom_edad = view.findViewById(R.id.edit_nom_edad);
        img_principal = view.findViewById(R.id.image_principal);


        store.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(user.getBirthDate());
                        int age = DateUtils.age(calendar);
                        String names = user.getNames() + ", " + age;

                        edit_nom_edad.setText(names);
                        edit_sobre_ti.setText(user.getDescription());

                        Glide.with(view).load(user.getProfilePic()).into(img_principal);
                    }
                }
            }
        });

        imagenes = new ArrayList<>();
        btnAddPhoto = view.findViewById(R.id.btn_edit_photos);

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                getActivity().startActivityForResult(i, GALLERY_ADD_CALLBACK_ID);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = new File(Environment.getExternalStorageDirectory() + "/" + UUID.randomUUID().toString() + ".png");
                Uri uri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName(), photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                getActivity().startActivityForResult(i, CAMERA_CALLBACK_ID);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                getActivity().startActivityForResult(i, GALLERY_CALLBACK_ID);
            }
        });


        store.collection("photos").whereEqualTo("userId", auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Photo> photos = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Photo photo = document.toObject(Photo.class);
                        photos.add(photo);
                    }
                    Collections.sort(photos);
                    for (Photo photo : photos) {
                        galleryAdapter.addPhoto(photo);
                    }
                }
            }
        });

        return view;
    }

    public File getPhotoFile() {
        return photoFile;
    }

    public void addPhoto(Photo photo) {
        galleryAdapter.addPhoto(photo);
    }
}
