package com.example.hingo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class HabitDao {

    private SQLiteDatabase database;
    private HabitDatabaseHelper dbHelper;

    public HabitDao(Context context) {
        dbHelper = new HabitDatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addHabit(Habit habit) {
        ContentValues values = new ContentValues();
        values.put(HabitDatabaseHelper.COLUMN_HABIT_NAME, habit.getName());
        values.put(HabitDatabaseHelper.COLUMN_TIMESTAMP, habit.getTimestamp());
        long newRowId = database.insert(HabitDatabaseHelper.TABLE_HABITS, null, values);
        Log.d("HabitDao", "Habit added with ID: " + newRowId);
        return newRowId;
    }

    public int deleteHabitByName(String name) {
        int deletedRows = database.delete(HabitDatabaseHelper.TABLE_HABITS,
                HabitDatabaseHelper.COLUMN_HABIT_NAME + " = ?",
                new String[]{name});
        Log.d("HabitDao", "Deleted " + deletedRows + " rows with name: " + name);
        return deletedRows;
    }

    public List<Habit> searchHabits(String query) {
        List<Habit> habits = new ArrayList<>();
        Cursor cursor = database.query(HabitDatabaseHelper.TABLE_HABITS,
                null,
                HabitDatabaseHelper.COLUMN_HABIT_NAME + " LIKE ?",
                new String[]{"%" + query + "%"},
                null, null, null);

        habits = parseCursor(cursor);
        cursor.close();
        Log.d("HabitDao", "Search found " + habits.size() + " habits");
        return habits;
    }

    public List<Habit> getAllHabits() {
        List<Habit> habits = new ArrayList<>();
        Cursor cursor = database.query(HabitDatabaseHelper.TABLE_HABITS,
                null, null, null, null, null, null);

        habits = parseCursor(cursor);
        cursor.close();
        Log.d("HabitDao", "Retrieved " + habits.size() + " habits");
        return habits;
    }

    private List<Habit> parseCursor(Cursor cursor) {
        List<Habit> habits = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(HabitDatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(HabitDatabaseHelper.COLUMN_HABIT_NAME));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(HabitDatabaseHelper.COLUMN_TIMESTAMP));
                habits.add(new Habit(id, name, timestamp));
            } while (cursor.moveToNext());
        }
        return habits;
    }
}