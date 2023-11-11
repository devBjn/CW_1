package com.example.hikermanagement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.TimePickerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;

public class UpdateObservation extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    EditText text_input,comment_input;
    TextView timeSelected;
    String obsId, text, time, date, comment, hikeId;
    Button buttonSelectedTimeUpdate, buttonUpdateObs, btnBackToDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);

        text_input = findViewById(R.id.editObservationTextUpdate);
        comment_input = findViewById(R.id.editObservationCommentUpdate);
        timeSelected = findViewById(R.id.observationSelectedTimeUpdate);
        buttonSelectedTimeUpdate = findViewById(R.id.buttonSelectedTimeUpdate);
        buttonUpdateObs = findViewById(R.id.buttonUpdateObs);
        btnBackToDetail = findViewById(R.id.btnBackToDetail);
        getAndSetData();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }

        buttonSelectedTimeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker update");
            }
        });

        buttonUpdateObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper DB = new DatabaseHelper(UpdateObservation.this);

                text = text_input.getText().toString().trim();
                time = timeSelected.getText().toString().trim();
                comment = comment_input.getText().toString().trim();

                DB.updateObservation(obsId, Integer.parseInt(hikeId), text, time, comment);

                Intent intent = new Intent(UpdateObservation.this, DetailActivity.class);
                startActivity(intent);
            }
        });
        btnBackToDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateObservation.this, DetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        timeSelected.setText(hour + " : " + minute);
    }

    void getAndSetData() {
        if(getIntent().hasExtra("obsId") && getIntent().hasExtra("text") && getIntent().hasExtra("time") && getIntent().hasExtra("comment")
                && getIntent().hasExtra("date")){
            Intent intent = getIntent();

            obsId = intent.getStringExtra("obsId");
            text = intent.getStringExtra("text");
            time = intent.getStringExtra("time");
            date = intent.getStringExtra("date");
            comment = intent.getStringExtra("comment");
            hikeId = intent.getStringExtra("hikeId");

            text_input.setText(text);
            timeSelected.setText(time);
            comment_input.setText(comment);

        } else {
            Toast.makeText(this, "No hike.", Toast.LENGTH_SHORT).show();
        }
    }
}