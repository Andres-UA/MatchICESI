package com.appmoviles.andres.matchicesi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kofigyan.stateprogressbar.StateProgressBar;

public class StepOneActivity extends AppCompatActivity {

    String[] descriptionData = {"Identity", "Movies", "Music", "Books", "walk"};

    private StateProgressBar stateProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_one);

        stateProgressBar =  findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);

    }
}
