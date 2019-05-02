package com.appmoviles.andres.matchicesi;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.appmoviles.andres.matchicesi.adapters.ItemListAdapter;
import com.appmoviles.andres.matchicesi.model.UserData;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class StepTwoActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener {

    private String[] descriptionData = {"Tu", "Peliculas", "Musica", "Libros", "Salidas"};
    private ArrayList<String> items = new ArrayList<>();

    private StateProgressBar stateProgressBar;
    private SpinnerDialog spinnerDialog;

    private SeekBar sbMovieRank;

    private ItemListAdapter movieListAdapter;
    private RecyclerView rvMoviesList;

    private MaterialButton btnAdd;
    private MaterialButton btnBack;
    private MaterialButton btnNext;

    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two);

        if (getIntent().getExtras().getSerializable("userData") != null) {
            userData = (UserData) getIntent().getExtras().getSerializable("userData"); //getActivity()
        } else {
            userData = new UserData();
        }

        items.add("Acción");
        items.add("Aventura");
        items.add("Comedias");
        items.add("Dramáticas");
        items.add("Terror");
        items.add("Musicales");
        items.add("Ciencia ficción");
        items.add("Guerra o bélicas");
        items.add("Películas del Oeste");
        items.add("Suspenso");

        stateProgressBar = findViewById(R.id.progress_step_two);
        stateProgressBar.setStateDescriptionData(descriptionData);

        sbMovieRank = findViewById(R.id.sb_movies);

        btnAdd = findViewById(R.id.two_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });

        btnBack = findViewById(R.id.two_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepTwoActivity.this, StepOneActivity.class);
                startActivity(intent);
            }
        });

        btnNext = findViewById(R.id.two_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int movieRank = sbMovieRank.getProgress();
                ArrayList<String> movies = movieListAdapter.getData();

                userData.setBookRank(movieRank);
                userData.setMovies(movies);

                Intent intent = new Intent(StepTwoActivity.this, StepThreeActivity.class);
                intent.putExtra("userData", userData);
                startActivity(intent);
            }
        });

        rvMoviesList = findViewById(R.id.list_movies);

        movieListAdapter = new ItemListAdapter();
        movieListAdapter.setListener(this);

        rvMoviesList.setLayoutManager(new LinearLayoutManager(this));
        rvMoviesList.setAdapter(movieListAdapter);
        rvMoviesList.setHasFixedSize(true);

        //spinnerDialog=new SpinnerDialog(StepOneActivity.this,items,"Select or Search City","Close Button Text");// With No Animation
        spinnerDialog = new SpinnerDialog(StepTwoActivity.this, items, "Selecciona o busca un genero", R.style.DialogAnimations_SmileWindow, "Cerrar");// With 	Animation

        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                movieListAdapter.addItem(item);
            }
        });

    }

    @Override
    public void onItemClick(String item) {
        movieListAdapter.deleteItem(item);
    }
}
