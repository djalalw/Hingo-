package com.example.hingo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HabitDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habits.db"; // Name der Datenbank
    private static final int DATABASE_VERSION = 2; // Aktualisieren Sie die Datenbankversion, wenn sich das Schema ändert

    static final String TABLE_HABITS = "habits"; // Name der Tabelle
    static final String COLUMN_ID = "_id"; // ID-Spalte
    static final String COLUMN_HABIT_NAME = "name"; // Name-Spalte
    static final String COLUMN_TIMESTAMP = "timestamp"; // Neue Spalte für den Zeitstempel

    // Konstruktor für den HabitDatabaseHelper
    public HabitDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Methode zum Erstellen der Datenbank
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL-Anweisung zum Erstellen der habits-Tabelle
        String createTable = "CREATE TABLE " + TABLE_HABITS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_HABIT_NAME + " TEXT NOT NULL, "
                + COLUMN_TIMESTAMP + " INTEGER NOT NULL);"; // Definieren der Zeitstempelspalte
        db.execSQL(createTable);
    }

    // Methode zum Aktualisieren der Datenbank
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Alte Tabelle löschen, falls vorhanden, und bei Upgrade neu erstellen
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABITS);
        onCreate(db);
    }
}
