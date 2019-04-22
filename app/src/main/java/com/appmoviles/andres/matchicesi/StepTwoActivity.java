package com.appmoviles.andres.matchicesi;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StepTwoActivity extends AppCompatActivity {

    MaterialButton btnBack;
    MaterialButton btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two);


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
                Intent intent = new Intent(StepTwoActivity.this, StepThreeActivity.class);
                startActivity(intent);
            }
        });

    }
}
