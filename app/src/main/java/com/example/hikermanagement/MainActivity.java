package com.example.hikermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;

    DatabaseHelper databaseHelper;

    ArrayList<String> id, name, location, date, difficulty, length, isParking, description;
    HikeAdapter hikeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.listView);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(MainActivity.this);
        id = new ArrayList<>();
        name = new ArrayList<>();
        location = new ArrayList<>();
        date = new ArrayList<>();
        difficulty = new ArrayList<>();
        length = new ArrayList<>();
        isParking = new ArrayList<>();
        description = new ArrayList<>();

        StoreData();
        Log.d(String.valueOf(location), "list");
        hikeAdapter = new HikeAdapter(MainActivity.this, id, name, location, date, isParking, length, difficulty, description);
        Log.d(hikeAdapter.toString(), "list");
        recyclerView.setAdapter(hikeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    void StoreData(){
        Cursor cursor = databaseHelper.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No hike .", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                location.add(cursor.getString(2));
                date.add(cursor.getString(3));
                isParking.add(cursor.getString(4));
                length.add(cursor.getString(5));
                difficulty.add(cursor.getString(6));
                description.add(cursor.getString(7));
            }
        }
    }
}