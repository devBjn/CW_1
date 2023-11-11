package com.example.hikermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {
    TextView nameTV, locationTV, lengthTV, difficultyTV, isParkingTV, descriptionTV, dateTV;
    Button buttonAddObservation;
    String name, location, length, description, difficulty, date, hikeId;
    boolean parkingAvailable;
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    TextView emptyText;
    ImageView emptyImage;
    ArrayList<String> obsId, obsText, obsTime, obsComment;
    ObservationAdapter observationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nameTV = findViewById(R.id.textName);
        locationTV = findViewById(R.id.textLocation);
        lengthTV = findViewById(R.id.textLength);
        difficultyTV = findViewById(R.id.textDifficulty);
        isParkingTV = findViewById(R.id.textParking);
        descriptionTV = findViewById(R.id.textDescription);
        dateTV = findViewById(R.id.textDate);
        buttonAddObservation = findViewById(R.id.buttonAddObservation);
        emptyImage = findViewById(R.id.emptyImageObservation);
        emptyText = findViewById(R.id.emptyTextObservation);
        recyclerView = findViewById(R.id.listObservation);
        getSetDataIntent();

        buttonAddObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, AddObservation.class);
                intent.putExtra("hikeId", hikeId);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(DetailActivity.this);
        obsId = new ArrayList<>();
        obsText = new ArrayList<>();
        obsTime = new ArrayList<>();
        obsComment = new ArrayList<>();

        storeDataObservation();
        observationAdapter = new ObservationAdapter(DetailActivity.this, obsId, obsText, obsTime, obsComment, date, hikeId);
        recyclerView.setAdapter(observationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
    }

    void getSetDataIntent() {
        Intent intent = getIntent();
        hikeId = intent.getStringExtra("hikeId");
        name = intent.getStringExtra("name");
        location = intent.getStringExtra("location");
        length = intent.getStringExtra("length");
        date = intent.getStringExtra("date");
        difficulty = intent.getStringExtra("difficulty");
        parkingAvailable = intent.getBooleanExtra("parkingAvailable", false);
        description = intent.getStringExtra("description");

        nameTV.setText(name);
        locationTV.setText(location);
        lengthTV.setText(length);
        descriptionTV.setText(description);
        difficultyTV.setText(difficulty);
        dateTV.setText(date);
        if(parkingAvailable){
            isParkingTV.setText(String.valueOf(true));
        } else {
            isParkingTV.setText(String.valueOf(false));
        }
    }

    void storeDataObservation() {
        Cursor cursor = databaseHelper.readAllObservation(Integer.parseInt(hikeId));
        if(cursor.getCount() == 0){
            emptyImage.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                obsId.add(cursor.getString(0));
                obsText.add(cursor.getString(2));
                obsTime.add(cursor.getString(3));
                obsComment.add(cursor.getString(4));
            }
            emptyImage.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);
        }
    }

}