package com.appmoviles.andres.matchicesi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmoviles.andres.matchicesi.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

public class UserFragment extends Fragment {

    private static final int CAMERA_CALLBACK_ID = 100;
    private static final int GALLERY_CALLBACK_ID = 101;

    private ImageView img_principal;
    private Button btn_take_pic;
    private File photoFile;
    private Button btn_open_gal;
    private EditText edit_nom_edad;
    private ArrayList<ImageView> imagenes;
    private Button btn_editar_photos;
    private EditText edit_sobre_ti;

    MaterialButton btnLogout;

    FirebaseFirestore storage;
    FirebaseAuth auth;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user, container, false);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseFirestore.getInstance();

        edit_sobre_ti = view.findViewById(R.id.edit_sobre_ti);
        edit_nom_edad = view.findViewById(R.id.edit_nom_edad);
        btn_open_gal = view.findViewById(R.id.btn_open_gal);
        btn_take_pic = view.findViewById(R.id.btn_take_pic);
        img_principal = view.findViewById(R.id.image_principal);
        btnLogout = view.findViewById(R.id.btn_logout);

        storage.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(user.getBirthDate());
                        int age = age(calendar);
                        String names = user.getNames() + ", " + age;

                        edit_nom_edad.setText(names);
                        edit_sobre_ti.setText(user.getDescription());

                        Glide.with(view).load(user.getProfilePic()).into(img_principal);
                    }
                }
            }
        });

        imagenes = new ArrayList<>();
        btn_editar_photos = view.findViewById(R.id.btn_edit_photos);

        btn_take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = new File(Environment.getExternalStorageDirectory() + "/" + UUID.randomUUID().toString() + ".png");
                Uri uri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName(), photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(i, CAMERA_CALLBACK_ID);
            }
        });

        btn_open_gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, GALLERY_CALLBACK_ID);

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }


    /*
    private void subirImagen() {
        try {
            StorageReference ref = storage.getReference().child("profiles").child(me.getTelefono());
            FileInputStream fis = new FileInputStream(photoFile);
            ref.putStream(fis).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    cargarFotoPerfil();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void cargarFotoPerfil() {
        StorageReference ref = storage.getReference().child("profiles").child(me.getTelefono());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileActivity.this).load(uri).into(img_principal);
            }
        });
    }
    */
}
