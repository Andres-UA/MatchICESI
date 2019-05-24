package com.appmoviles.andres.matchicesi;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.appmoviles.andres.matchicesi.adapters.ItemListAdapter;
import com.appmoviles.andres.matchicesi.model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class StepOneActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener {

    private String[] descriptionData = {"Tu", "Peliculas", "Musica", "Libros", "Salidas"};
    private ArrayList<String> items = new ArrayList<>();

    private ArrayList<String> descriptors = new ArrayList<>();

    private StateProgressBar stateProgressBar;
    private SpinnerDialog spinnerDialog;

    private ItemListAdapter identityListAdapter;
    private RecyclerView rvIdentityList;

    private MaterialButton btnAdd;
    private MaterialButton btnNext;

    private UserData userData;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_one);

        auth = FirebaseAuth.getInstance();

        if (getIntent().getExtras().getSerializable("userData") != null) {
            userData = (UserData) getIntent().getExtras().getSerializable("userData");
            if (userData.getDescriptors() != null) {
                descriptors = userData.getDescriptors();
            }
        }

        items.add("Abierto");
        items.add("Chistoso");
        items.add("Comprensivo");
        items.add("Extrovertido");
        items.add("Generoso");
        items.add("Introvertido");
        items.add("Romántico");
        items.add("Serio");

        stateProgressBar = findViewById(R.id.progress_step_one);
        stateProgressBar.setStateDescriptionData(descriptionData);

        btnAdd = findViewById(R.id.one_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });

        btnNext = findViewById(R.id.one_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userData.setId(auth.getCurrentUser().getUid());
                userData.setDescriptors(identityListAdapter.getData());
                Intent intent = new Intent(StepOneActivity.this, StepTwoActivity.class);
                intent.putExtra("userData", userData);
                startActivity(intent);
            }
        });

        rvIdentityList = findViewById(R.id.list_identity);

        identityListAdapter = new ItemListAdapter();
        identityListAdapter.setData(descriptors);
        identityListAdapter.setListener(this);

        rvIdentityList.setLayoutManager(new LinearLayoutManager(this));
        rvIdentityList.setAdapter(identityListAdapter);
        rvIdentityList.setHasFixedSize(true);

        spinnerDialog = new SpinnerDialog(StepOneActivity.this, items, "Seleccione o busque un descriptor", R.style.DialogAnimations_SmileWindow, "Cerrar");// With 	Animation

        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                identityListAdapter.addItem(item);
            }
        });
    }

    public void initialize() {

    }

    @Override
    public void onItemClick(String item) {
        identityListAdapter.deleteItem(item);
    }
}
