package com.example.hingo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HabitDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habits.db";
    private static final int DATABASE_VERSION = 2; // Update database version when schema changes

    static final String TABLE_HABITS = "habits";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_HABIT_NAME = "name";
    static final String COLUMN_TIMESTAMP = "timestamp"; // New column for timestamp

    public HabitDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create the habits table
        String createTable = "CREATE TABLE " + TABLE_HABITS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_HABIT_NAME + " TEXT NOT NULL, "
                + COLUMN_TIMESTAMP + " INTEGER NOT NULL);"; // Define timestamp column
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table if exists and recreate on upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABITS);
        onCreate(db);
    }
}
