package com.appmoviles.andres.matchicesi;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appmoviles.andres.matchicesi.adapters.ItemListAdapter;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class StepThreeActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener {

    private String[] descriptionData = {"Tu", "Peliculas", "Musica", "Libros", "Salidas"};
    private ArrayList<String> items = new ArrayList<>();

    private StateProgressBar stateProgressBar;
    private SpinnerDialog spinnerDialog;

    private ItemListAdapter identityListAdapter;
    private RecyclerView rvIdentityList;

    private MaterialButton btnAdd;
    private MaterialButton btnBack;
    private MaterialButton btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_three);

        items.add("Blues");
        items.add("Country");
        items.add("Cumbia");
        items.add("Disco");
        items.add("Electrónica");
        items.add("Flamenco");
        items.add("Folk");
        items.add("Gospel");
        items.add("Heavy Metal");
        items.add("Hip Hop");
        items.add("Indie");
        items.add("Jazz");
        items.add("Merengue");
        items.add("Pop");
        items.add("Punk");
        items.add("Ranchera");
        items.add("Rap");
        items.add("Reggae");
        items.add("Reggaeton");
        items.add("Rhythm and Blues");
        items.add("Rock");
        items.add("Rock and Roll");
        items.add("Salsa");
        items.add("Son");
        items.add("Tango");
        items.add("Vallenato");

        stateProgressBar = findViewById(R.id.progress_step_three);
        stateProgressBar.setStateDescriptionData(descriptionData);

        btnAdd = findViewById(R.id.three_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });

        btnBack = findViewById(R.id.three_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepThreeActivity.this, StepTwoActivity.class);
                startActivity(intent);
            }
        });

        btnNext = findViewById(R.id.three_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepThreeActivity.this, StepFourActivity.class);
                startActivity(intent);
            }
        });

        rvIdentityList = findViewById(R.id.list_music);

        identityListAdapter = new ItemListAdapter();
        identityListAdapter.setListener(this);

        rvIdentityList.setLayoutManager(new LinearLayoutManager(this));
        rvIdentityList.setAdapter(identityListAdapter);
        rvIdentityList.setHasFixedSize(true);

        //spinnerDialog=new SpinnerDialog(StepOneActivity.this,items,"Select or Search City","Close Button Text");// With No Animation
        spinnerDialog = new SpinnerDialog(StepThreeActivity.this, items, "Selecciona o busca un genero", R.style.DialogAnimations_SmileWindow, "Cerrar");// With 	Animation

        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                identityListAdapter.addItem(item);
            }
        });

    }

    @Override
    public void onItemClick(String item) {
        identityListAdapter.deleteItem(item);
    }
}
