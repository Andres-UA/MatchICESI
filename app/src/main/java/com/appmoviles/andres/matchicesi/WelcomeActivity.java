package com.appmoviles.andres.matchicesi;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.appmoviles.andres.matchicesi.model.UserData;

public class WelcomeActivity extends AppCompatActivity {

    MaterialButton btnBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnBegin = findViewById(R.id.btn_begin);

        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData userData = new UserData();
                Intent intent = new Intent(WelcomeActivity.this, StepOneActivity.class);
                intent.putExtra("userData", userData);
                startActivity(intent);
                finish();
            }
        });

    }
}
