package com.example.hikermanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "HikerManagement.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_HIKES = "hikes";
    private static final String ID = "_id";
    private static final String NAME = "name";
    private static final String LOCATION = "location";
    private static final String DAY_OF_HIKE = "day_of_hike";
    private static final String PARKING = "parking";
    private static final String LENGTH = "length_of_hike";
    private static final String DIFFICULTY = "difficulty";
    private static final String DESCRIPTION = "description";

    private static final String TABLE_OBSERVATIONS = "observations";
    private static final String OBSERVATION_ID = "observation_id";
    private static final String HIKE_ID = "hike_id";
    private static final String OBSERVATION_TEXT = "observation_text";
    private static final String OBSERVATION_TIME = "observation_time";
    private static final String ADDITIONAL_COMMENTS = "additional_comments";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String hike_query =
                "CREATE TABLE " + TABLE_HIKES +
                        " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NAME + " TEXT, " +
                        LOCATION + " TEXT, " +
                        DAY_OF_HIKE + " TEXT, " +
                        PARKING + " TEXT, " +
                        LENGTH + " TEXT, " +
                        DIFFICULTY + " TEXT, " +
                        DESCRIPTION + " TEXT) ;";

        String observation_query =
                "CREATE TABLE " + TABLE_OBSERVATIONS +
                        " (" + OBSERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        HIKE_ID + " INTEGER, " +
                        OBSERVATION_TEXT + " TEXT, " +
                        OBSERVATION_TIME + " TEXT, " +
                        ADDITIONAL_COMMENTS + " TEXT) ;";

        db.execSQL(hike_query);
        db.execSQL(observation_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        onCreate(db);
    }

    //hike
    void addHike(String name, String location, String date, String length,
                 String difficulty, String description, String parking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NAME, name);
        cv.put(LOCATION, location);
        cv.put(DAY_OF_HIKE, date);
        cv.put(LENGTH, length);
        cv.put(DIFFICULTY, difficulty);
        cv.put(DESCRIPTION, description);
        cv.put(PARKING, parking);

        long result = db.insert(TABLE_HIKES, null, cv);

        if(result == -1){
            Toast.makeText(context, "Added Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_HIKES;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateHike(String id, String name, String location, String date, String length,
                 String difficulty, String description, String parking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NAME, name);
        cv.put(LOCATION, location);
        cv.put(DAY_OF_HIKE, date);
        cv.put(LENGTH, length);
        cv.put(DIFFICULTY, difficulty);
        cv.put(DESCRIPTION, description);
        cv.put(PARKING, parking);

        long result = db.update(TABLE_HIKES, cv, "_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Updated Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOne(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_HIKES, "_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Deleted Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Deleted Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HIKES);
    }

    //observation
    void addObservation(int hike_id, String observation_text, String observation_time, String observation_comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(HIKE_ID, hike_id);
        cv.put(OBSERVATION_TEXT, observation_text);
        cv.put(OBSERVATION_TIME, observation_time);
        cv.put(ADDITIONAL_COMMENTS, observation_comment);

        long result = db.insert(TABLE_OBSERVATIONS, null, cv);

        if(result == -1){
            Toast.makeText(context, "Added Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllObservation(int hikeId) {
        String query = "SELECT * FROM " + TABLE_OBSERVATIONS + " WHERE " + HIKE_ID + " = " + hikeId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateObservation(String id,int hike_id, String observation_text, String observation_time, String observation_comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HIKE_ID, hike_id);
        cv.put(OBSERVATION_TEXT, observation_text);
        cv.put(OBSERVATION_TIME, observation_time);
        cv.put(ADDITIONAL_COMMENTS, observation_comment);

        long result = db.update(TABLE_OBSERVATIONS, cv, "observation_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Updated Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneObservation(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_OBSERVATIONS, "observation_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Deleted Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Deleted Successfully !", Toast.LENGTH_SHORT).show();
        }
    }
}
