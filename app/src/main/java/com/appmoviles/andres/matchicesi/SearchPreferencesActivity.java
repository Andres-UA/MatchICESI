package com.appmoviles.andres.matchicesi;

import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.appmoviles.andres.matchicesi.model.SearchPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.util.HashMap;
import java.util.Map;

public class SearchPreferencesActivity extends AppCompatActivity {

    private static final String TAG = "0";

    private CheckBox cbMale;
    private CheckBox cbFemale;
    private Button btnCancel;
    private MaterialButton btnAccept;
    private RangeSeekBar rangeSeekBar;

    SearchPreferences searchPreferences;

    private int min;
    private int max;

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_preferences);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnCancel = findViewById(R.id.preferences_btn_cancel);
        btnAccept = findViewById(R.id.preferences_btn_accept);

        rangeSeekBar = findViewById(R.id.age_range);
        rangeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                max = (int) Math.floor(rightValue);
                min = (int) Math.floor(leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }
        });

        cbMale = findViewById(R.id.cb_male);
        cbFemale = findViewById(R.id.cb_female);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPreferences = new SearchPreferences(min, max, cbMale.isChecked(), cbFemale.isChecked());
                db.collection("search_preferences").document(auth.getCurrentUser().getUid())
                        .update(
                                "minAge", searchPreferences.getMinAge(),
                                "maxAge", searchPreferences.getMaxAge(),
                                "male", searchPreferences.isMale(),
                                "female", searchPreferences.isFemale()
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        load();
    }

    private void load() {
        db.collection("search_preferences").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                searchPreferences = documentSnapshot.toObject(SearchPreferences.class);
                cbFemale.setChecked(searchPreferences.isFemale());
                cbMale.setChecked(searchPreferences.isMale());
                rangeSeekBar.setValue(searchPreferences.getMinAge(), searchPreferences.getMaxAge());
            }
        });
    }

}
