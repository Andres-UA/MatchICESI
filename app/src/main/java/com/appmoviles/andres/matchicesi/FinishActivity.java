package com.appmoviles.andres.matchicesi;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appmoviles.andres.matchicesi.model.SearchPreferences;
import com.appmoviles.andres.matchicesi.model.UserData;
import com.appmoviles.andres.matchicesi.util.Util;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class FinishActivity extends AppCompatActivity {

    private static final int CAMERA_CALLBACK_ID = 100;
    private static final int GALLERY_CALLBACK_ID = 101;

    private MaterialButton btnFinish;
    private ExtendedEditText description;

    private ImageView img_principal;
    private Button btn_take_pic;
    private File photoFile;
    private Button btn_open_gal;

    FirebaseAuth auth;
    FirebaseFirestore store;
    FirebaseStorage storage;

    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        auth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        description = findViewById(R.id.description);
        btn_open_gal = findViewById(R.id.open_gall);
        btn_take_pic = findViewById(R.id.open_cam);
        img_principal = findViewById(R.id.main_image);

        if (getIntent().getExtras().getSerializable("userData") != null) {
            userData = (UserData) getIntent().getExtras().getSerializable("userData");
        } else {
            userData = new UserData();
        }

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 0);

        btnFinish = findViewById(R.id.btn_begin);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uid = auth.getCurrentUser().getUid();

                store.collection("user_data").document(uid).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        store.collection("users").document(uid).update("firstLogin", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                store.collection("users").document(uid).update("description", description.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        SearchPreferences searchPreferences = new SearchPreferences(18, 30, true, true);
                                        store.collection("search_preferences").document(auth.getCurrentUser().getUid()).set(searchPreferences).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(FinishActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        btn_take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = new File(Environment.getExternalStorageDirectory() + "/" + UUID.randomUUID().toString() + ".png");
                Uri uri = FileProvider.getUriForFile(FinishActivity.this, getPackageName(), photoFile);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        Uri uri = FileProvider.getUriForFile(FinishActivity.this, getPackageName(), photoFile);
        String destinationFileName = UUID.randomUUID().toString();

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);

        UCrop.Options options = new UCrop.Options();
        options.setToolbarWidgetColor(Color.parseColor("#E21662"));

        uCrop.withOptions(options);
        uCrop.start(FinishActivity.this);
    }

    private void loadImage() {
        StorageReference ref = storage.getReference().child("profiles").child(auth.getCurrentUser().getUid());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(FinishActivity.this).load(uri).into(img_principal);
                store.collection("users").document(auth.getCurrentUser().getUid()).update("profilePic", uri.toString());
            }
        });
    }

}
