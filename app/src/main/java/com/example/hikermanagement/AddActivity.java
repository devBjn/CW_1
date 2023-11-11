package com.example.hikermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private EditText name_hike_input, location_input, length_input, description_input;
    private DatePicker datePicker;
    private RadioGroup group_parking;
    private Spinner spinner;
    private Button buttonAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name_hike_input = findViewById(R.id.editNameOfHike);
        location_input = findViewById(R.id.editLocation);
        length_input = findViewById(R.id.editLengthOfHike);
        description_input = findViewById(R.id.editDescription);
        group_parking = findViewById(R.id.radioGroupParking);
        spinner = findViewById(R.id.spinner);
        datePicker = findViewById(R.id.datePicker);
        buttonAdd = findViewById(R.id.buttonAdd);

        getSetDataIntent();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_hike_input.getText().toString().trim();
                String location = location_input.getText().toString().trim();
                String date = String.format("%02d-%02d-%d",
                        datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());
                    String length_hike = length_input.getText().toString().trim();
                    String level_difficulty = spinner.getSelectedItem().toString().trim();
                    String description = description_input.getText().toString().trim();
                    int selectedRadioButtonId = group_parking.getCheckedRadioButtonId();
                    boolean is_parking = selectedRadioButtonId == R.id.radioButtonYes;
                    // Popup alert
                    if (name.isEmpty() || location.isEmpty() || date.isEmpty() ||
                            length_hike.isEmpty() || level_difficulty.isEmpty()) {
                        Toast.makeText(AddActivity.this, "Fields are required !", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Intent intent = new Intent(AddActivity.this, Prompt.class);

                        intent.putExtra("name", name);
                        intent.putExtra("location", location);
                        intent.putExtra("date", date);
                        intent.putExtra("parking", is_parking);
                        intent.putExtra("length", length_hike);
                        intent.putExtra("difficulty", level_difficulty);
                        intent.putExtra("description", description);
                        startActivity(intent);
                }
            }
        });
    }
    void getSetDataIntent() {
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");
        String length = intent.getStringExtra("length");
        String date = intent.getStringExtra("date");
        String difficulty = intent.getStringExtra("difficulty");
        boolean parkingAvailable = intent.getBooleanExtra("parkingAvailable", false);
        String description = intent.getStringExtra("description");

        name_hike_input.setText(name);
        location_input.setText(location);
        length_input.setText(length);
        description_input.setText(description);

        if(date == null) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            datePicker.init(year, month, day,null);
        }
        else {
            String[] dateParts = date.split("-");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int year = Integer.parseInt(dateParts[2]);
            datePicker.init(year, month, day, null);
        }

        String[] difficultyLevels = getResources().getStringArray(R.array.LevelDifficult);
        int selectedPosition = Arrays.asList(difficultyLevels).indexOf(difficulty);

        spinner.setSelection(selectedPosition);

        if (parkingAvailable) {
            group_parking.check(R.id.radioButtonYes);
        } else {
            group_parking.check(R.id.radioButtonNo);
        }
    }
}