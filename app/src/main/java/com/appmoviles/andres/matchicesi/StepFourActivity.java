package com.appmoviles.andres.matchicesi;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appmoviles.andres.matchicesi.adapters.ItemListAdapter;
import com.appmoviles.andres.matchicesi.model.UserData;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class StepFourActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener {

    private String[] descriptionData = {"Tu", "Peliculas", "Musica", "Libros", "Salidas"};
    private ArrayList<String> items = new ArrayList<>();

    private ArrayList<String> books = new ArrayList<>();
    private int bookRank = 1;

    private StateProgressBar stateProgressBar;
    private SpinnerDialog spinnerDialog;

    private SeekBar sbBookRank;
    private TextView tvBookValue;

    private ItemListAdapter bookListAdapter;
    private RecyclerView rvBookList;

    private MaterialButton btnAdd;
    private MaterialButton btnBack;
    private MaterialButton btnNext;

    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_four);

        if (getIntent().getExtras().getSerializable("userData") != null) {
            userData = (UserData) getIntent().getExtras().getSerializable("userData");
            if (userData.getBooks() != null) {
                books = userData.getBooks();
            }
            if (userData.getBookRank() != 1) {
                bookRank = userData.getBookRank();
            }
        }

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

        sbBookRank = findViewById(R.id.sb_book);
        sbBookRank.setProgress(bookRank - 1);

        tvBookValue = findViewById(R.id.book_value);
        tvBookValue.setText(bookRank + "");

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
                setData();
                Intent intent = new Intent(StepFourActivity.this, StepThreeActivity.class);
                intent.putExtra("userData", userData);
                startActivity(intent);
            }
        });

        btnNext = findViewById(R.id.four_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                Intent intent = new Intent(StepFourActivity.this, StepFiveActivity.class);
                intent.putExtra("userData", userData);
                startActivity(intent);
            }
        });

        rvBookList = findViewById(R.id.list_books);

        bookListAdapter = new ItemListAdapter();
        bookListAdapter.setData(books);
        bookListAdapter.setListener(this);

        rvBookList.setLayoutManager(new LinearLayoutManager(this));
        rvBookList.setAdapter(bookListAdapter);
        rvBookList.setHasFixedSize(true);

        sbBookRank.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                bookRank = progressChangedValue + 1;
                tvBookValue.setText("" + bookRank);
            }
        });

        //spinnerDialog=new SpinnerDialog(StepOneActivity.this,items,"Select or Search City","Close Button Text");// With No Animation
        spinnerDialog = new SpinnerDialog(StepFourActivity.this, items, "Select or Search City", R.style.DialogAnimations_SmileWindow, "Cerrar");// With 	Animation

        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                bookListAdapter.addItem(item);
            }
        });

    }

    public void setData() {
        ArrayList<String> books = bookListAdapter.getData();
        userData.setBookRank(bookRank);
        userData.setBooks(books);
    }

    @Override
    public void onItemClick(String item) {
        bookListAdapter.deleteItem(item);
    }
}
