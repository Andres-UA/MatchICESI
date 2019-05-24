package com.appmoviles.andres.matchicesi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.appmoviles.andres.matchicesi.adapters.ItemListAdapter;
import com.appmoviles.andres.matchicesi.adapters.ItemMoveCallback;
import com.appmoviles.andres.matchicesi.adapters.SpecialListAdapter;
import com.appmoviles.andres.matchicesi.adapters.StartDragListener;
import com.appmoviles.andres.matchicesi.model.UserData;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class StepFiveActivity extends AppCompatActivity implements StartDragListener {

    private String[] descriptionData = {"Tu", "Peliculas", "Musica", "Libros", "Salidas"};
    private ArrayList<String> funs = new ArrayList<>();

    private StateProgressBar stateProgressBar;

    private SpecialListAdapter funListAdapter;
    private RecyclerView rvFunList;

    private ArrayList<String> dataset = new ArrayList<>();

    private MaterialButton btnBack;
    private MaterialButton btnNext;

    ItemTouchHelper touchHelper;

    private UserData userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_five);

        if (getIntent().getExtras().getSerializable("userData") != null) {
            userData = (UserData) getIntent().getExtras().getSerializable("userData");
            if (userData.getFuns() != null) {
                funs = userData.getFuns();
            }
        }

        stateProgressBar = findViewById(R.id.progress_step_five);
        stateProgressBar.setStateDescriptionData(descriptionData);

        btnBack = findViewById(R.id.five_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> funs = funListAdapter.getData();
                userData.setFuns(funs);
                Intent intent = new Intent(StepFiveActivity.this, StepFourActivity.class);
                intent.putExtra("userData", userData);
                startActivity(intent);
            }
        });

        btnNext = findViewById(R.id.five_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> funs = funListAdapter.getData();
                userData.setFuns(funs);
                Intent intent = new Intent(StepFiveActivity.this, FinishActivity.class);
                intent.putExtra("userData", userData);
                startActivity(intent);
            }
        });

        rvFunList = findViewById(R.id.list_fun);


        dataset.add("Salir a cine/teatro");
        dataset.add("Salir a discoteca");
        dataset.add("Salir a comer");
        dataset.add("Salir a caminar");

        if (!funs.isEmpty()) {
            dataset = funs;
        }
        funListAdapter = new SpecialListAdapter(dataset, this);

        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(funListAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvFunList);

        rvFunList.setLayoutManager(new LinearLayoutManager(this));
        rvFunList.setAdapter(funListAdapter);
        rvFunList.setHasFixedSize(false);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvFunList.addItemDecoration(divider);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(dataset, position_dragged, position_target);
                funListAdapter.notifyItemMoved(position_dragged, position_target);


                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }
        });

        helper.attachToRecyclerView(rvFunList);

    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

}
