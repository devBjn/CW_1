package com.example.hikermanagement;

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

public class AddObservation extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    Button buttonSelectedTime, buttonAddObs, buttonBack;
    TextView observationSelectedTime;
    String observationText, observationTime, observationComment, hikeId;
    EditText observationTextInput, observationCommentInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        buttonAddObs = findViewById(R.id.buttonAddObs);
        buttonSelectedTime = findViewById(R.id.buttonSelectedTime);
        observationTextInput = findViewById(R.id.editObservationText);
        observationCommentInput = findViewById(R.id.editObservationComment);
        buttonBack = findViewById(R.id.buttonBackToDetail);

        Intent intent = getIntent();
        hikeId = intent.getStringExtra("hikeId");
        buttonSelectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        buttonAddObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                observationText = observationTextInput.getText().toString().trim();
                observationTime = observationSelectedTime.getText().toString().trim();
                observationComment = observationCommentInput.getText().toString().trim();

                DatabaseHelper DB = new DatabaseHelper(AddObservation.this);
                DB.addObservation(Integer.parseInt(hikeId),observationText, observationTime, observationComment);

                Intent intent = new Intent(AddObservation.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddObservation.this, DetailActivity.class);
                startActivity(intent);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        observationSelectedTime = findViewById(R.id.observationSelectedTime);
        observationSelectedTime.setText(hour + " : " + minute);
    }
}