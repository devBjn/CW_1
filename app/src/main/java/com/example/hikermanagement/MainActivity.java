package com.example.hikermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;

    DatabaseHelper databaseHelper;

    ArrayList<String> id, name, location, date, difficulty, length, isParking, description;
    HikeAdapter hikeAdapter;
    TextView emptyText;
    ImageView emptyImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.listView);
        add_button = findViewById(R.id.add_button);
        emptyText = findViewById(R.id.emptyText);
        emptyImage = findViewById(R.id.emptyImage);
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
        hikeAdapter = new HikeAdapter(MainActivity.this, id, name, location, date, isParking, length, difficulty, description);
        recyclerView.setAdapter(hikeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    void StoreData(){
        Cursor cursor = databaseHelper.readAllData();
        if(cursor.getCount() == 0){
            emptyImage.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.VISIBLE);
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
            emptyImage.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_all, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.deleteAll){
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete all?");
        builder.setMessage("Are you sure you want to delete all?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseHelper.deleteAll();

                id.clear();
                name.clear();
                location.clear();
                date.clear();
                isParking.clear();
                length.clear();
                difficulty.clear();
                description.clear();

                hikeAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}