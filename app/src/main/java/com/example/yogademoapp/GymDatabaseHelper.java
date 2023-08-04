package com.example.yogademoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GymDatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "fitness.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    private static final String TABLE_GYM_DETAILS = "gym_details";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_INSTRUCTOR_NAME = "instructor_name";
    private static final String COLUMN_GYM_NUMBER = "gym_number";
    private static final String COLUMN_EXPERIENCE = "experience";
    private static final String COLUMN_MOBILE_NO = "mobile_no";
    private static final String COLUMN_FEES = "fees";

    // Create table query
    private static final String CREATE_GYM_DETAILS_TABLE =
            "CREATE TABLE " + TABLE_GYM_DETAILS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_INSTRUCTOR_NAME + " TEXT, " +
                    COLUMN_GYM_NUMBER + " TEXT, " +
                    COLUMN_EXPERIENCE + " TEXT, " +
                    COLUMN_MOBILE_NO + " TEXT, " +
                    COLUMN_FEES + " INTEGER)";

    public GymDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GYM_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method will be called if the database version changes.
        // You can handle database schema changes here.
        // For now, we will just drop and recreate the table.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GYM_DETAILS);
        onCreate(db);
    }

    // Method to insert a new gym details record
    public long insertGymDetails(String instructorName, String gymNumber, String experience,
                                 String mobileNo, int fees) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INSTRUCTOR_NAME, instructorName);
        values.put(COLUMN_GYM_NUMBER, gymNumber);
        values.put(COLUMN_EXPERIENCE, experience);
        values.put(COLUMN_MOBILE_NO, mobileNo);
        values.put(COLUMN_FEES, fees);

        // Insert the new row, and return the primary key value of the new row
        return db.insert(TABLE_GYM_DETAILS, null, values);
    }

    // Method to retrieve all gym details records
    public List<GymdbDetails> getAllGymDetails() {
        List<GymdbDetails> gymDetailsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to retrieve all rows from the table
        Cursor cursor = db.query(
                TABLE_GYM_DETAILS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Iterate through the cursor and populate the list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String instructorName = cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUCTOR_NAME));
                String gymNumber = cursor.getString(cursor.getColumnIndex(COLUMN_GYM_NUMBER));
                String experience = cursor.getString(cursor.getColumnIndex(COLUMN_EXPERIENCE));
                String mobileNo = cursor.getString(cursor.getColumnIndex(COLUMN_MOBILE_NO));
                int fees = cursor.getInt(cursor.getColumnIndex(COLUMN_FEES));

                GymdbDetails GymdbDetails = new GymdbDetails(id, instructorName, gymNumber, experience, mobileNo, fees);
                gymDetailsList.add(GymdbDetails);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return gymDetailsList;
    }

    // Method to delete a gym details record
    public int deleteGymDetails(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_GYM_DETAILS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
