package com.example.hikermanagement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    private EditText name_hike_input, location_input, length_input, description_input;
    private DatePicker datePicker;
    private RadioGroup group_parking;
    private Spinner spinner;
    private Button buttonSave;
    private String id, name, location, length, date, difficulty, description;
    private boolean isParking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        name_hike_input = findViewById(R.id.editNameOfHikeUpdate);
        location_input = findViewById(R.id.editLocationUpdate);
        length_input = findViewById(R.id.editLengthOfHikeUpdate);
        description_input = findViewById(R.id.editDescriptionUpdate);
        group_parking = findViewById(R.id.radioGroupParkingUpdate);
        spinner = findViewById(R.id.spinnerUpdate);
        datePicker = findViewById(R.id.datePickerUpdate);
        buttonSave = findViewById(R.id.buttonUpdate);

        getSetDataIntent();
        //set title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
        }
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper DB = new DatabaseHelper(UpdateActivity.this);

                name = name_hike_input.getText().toString().trim();
                location = location_input.getText().toString().trim();
                date = String.format("%02d-%02d-%d",
                        datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());
                length = length_input.getText().toString().trim();
                difficulty = spinner.getSelectedItem().toString().trim();
                description = description_input.getText().toString().trim();
                int selectedRadioButtonId = group_parking.getCheckedRadioButtonId();
                isParking = selectedRadioButtonId == R.id.radioButtonYes;

                DB.updateHike(id,name, location, date, length, difficulty , description, String.valueOf(isParking));

                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    void getSetDataIntent() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("location") && getIntent().hasExtra("date")
        && getIntent().hasExtra("parkingAvailable") && getIntent().hasExtra("length") && getIntent().hasExtra("difficulty") && getIntent().hasExtra("description")){
            Intent intent = getIntent();

            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            location = intent.getStringExtra("location");
            length = intent.getStringExtra("length");
            date = intent.getStringExtra("date");
            difficulty = intent.getStringExtra("difficulty");
            isParking = intent.getBooleanExtra("parkingAvailable", false);
            description = intent.getStringExtra("description");

            name_hike_input.setText(name);
            location_input.setText(location);
            length_input.setText(length);
            description_input.setText(description);

            //set value for datePicker
            if(date == null) {
                // get the current date
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

            //set value for spinner
            String[] difficultyLevels = getResources().getStringArray(R.array.LevelDifficult);
            int selectedPosition = Arrays.asList(difficultyLevels).indexOf(difficulty);

            spinner.setSelection(selectedPosition);

            //set value for group_parking
            if (isParking) {
                group_parking.check(R.id.radioButtonYes);
            } else {
                group_parking.check(R.id.radioButtonNo);
            }
        } else {
            Toast.makeText(this, "No hike.", Toast.LENGTH_SHORT).show();
        }
    }
}