package com.example.hikermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Prompt extends AppCompatActivity {

    private TextView nameTV, locationTV, lengthTV, dateTV, difficultyTV, parkingTV, descriptionTV;
    private Button buttonBack, buttonSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");
        String length = intent.getStringExtra("length");
        String date = intent.getStringExtra("date");
        String difficulty = intent.getStringExtra("difficulty");
        boolean isParking = intent.getBooleanExtra("parking", false);
        String description = intent.getStringExtra("description");

        nameTV = findViewById(R.id.textViewName);
        locationTV = findViewById(R.id.textViewLocation);
        lengthTV = findViewById(R.id.textViewLength);
        dateTV = findViewById(R.id.textViewDate);
        difficultyTV = findViewById(R.id.textViewDifficulty);
        parkingTV = findViewById(R.id.textViewParking);
        descriptionTV = findViewById(R.id.textViewDescription);

        nameTV.setText(name);
        locationTV.setText(location);
        lengthTV.setText(length);
        dateTV.setText(date);
        difficultyTV.setText(difficulty);
        descriptionTV.setText(description);
        parkingTV.setText(isParking ? "Yes" : "No");

        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonBack = findViewById(R.id.buttonBack);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper DB = new DatabaseHelper(Prompt.this);
                DB.addHike(name, location, date, length, difficulty , description, String.valueOf(isParking));

                Intent intent = new Intent(Prompt.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Prompt.this, AddActivity.class);

                intent.putExtra("name", name);
                intent.putExtra("location", location);
                intent.putExtra("date", date);
                intent.putExtra("parkingAvailable", isParking);
                intent.putExtra("length", length);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("description", description);
                startActivity(intent);
            }
        });
    }
}