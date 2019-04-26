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

public class StepFourActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener {

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
        setContentView(R.layout.activity_step_four);

        items.add("La novela de ciencia ficción");
        items.add("La novela gótica");
        items.add("La novela policíaca");
        items.add("La novela distópica");
        items.add("La novela utópica");
        items.add("La novela fantástica");
        items.add("La novela de thriller/suspenso");
        items.add("La novela de terror");

        stateProgressBar = findViewById(R.id.progress_step_four);
        stateProgressBar.setStateDescriptionData(descriptionData);

        btnAdd = findViewById(R.id.four_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });

        btnBack = findViewById(R.id.four_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepFourActivity.this, StepThreeActivity.class);
                startActivity(intent);
            }
        });

        btnNext = findViewById(R.id.four_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepFourActivity.this, StepFiveActivity.class);
                startActivity(intent);
            }
        });

        rvIdentityList = findViewById(R.id.list_books);

        identityListAdapter = new ItemListAdapter();
        identityListAdapter.setListener(this);

        rvIdentityList.setLayoutManager(new LinearLayoutManager(this));
        rvIdentityList.setAdapter(identityListAdapter);
        rvIdentityList.setHasFixedSize(true);

        //spinnerDialog=new SpinnerDialog(StepOneActivity.this,items,"Select or Search City","Close Button Text");// With No Animation
        spinnerDialog = new SpinnerDialog(StepFourActivity.this, items, "Select or Search City", R.style.DialogAnimations_SmileWindow, "Cerrar");// With 	Animation

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
