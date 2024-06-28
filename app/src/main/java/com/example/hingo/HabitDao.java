package com.example.hingo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    // Add a new habit to the database
    public void addHabit(Habit habit) {
        ContentValues values = new ContentValues();
        values.put(HabitDatabaseHelper.COLUMN_HABIT_NAME, habit.getName());
        values.put(HabitDatabaseHelper.COLUMN_TIMESTAMP, habit.getTimestamp()); // Add timestamp value
        database.insert(HabitDatabaseHelper.TABLE_HABITS, null, values);
    }

    // Delete a habit from the database by its ID
    public void deleteHabit(long id) {
        database.delete(HabitDatabaseHelper.TABLE_HABITS,
                HabitDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Update an existing habit in the database
    public void updateHabit(Habit habit) {
        ContentValues values = new ContentValues();
        values.put(HabitDatabaseHelper.COLUMN_HABIT_NAME, habit.getName());
        values.put(HabitDatabaseHelper.COLUMN_TIMESTAMP, habit.getTimestamp()); // Update timestamp value
        database.update(HabitDatabaseHelper.TABLE_HABITS,
                values,
                HabitDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(habit.getId())});
    }

    // Search habits by name containing the specified query
    public List<Habit> searchHabits(String query) {
        List<Habit> habits = new ArrayList<>();
        Cursor cursor = database.query(HabitDatabaseHelper.TABLE_HABITS,
                null,
                HabitDatabaseHelper.COLUMN_HABIT_NAME + " LIKE ?",
                new String[]{"%" + query + "%"},
                null, null, null);

        habits = parseCursor(cursor);
        cursor.close();
        return habits;
    }

    // Fetch all habits from the database
    public List<Habit> getAllHabits() {
        List<Habit> habits = new ArrayList<>();
        Cursor cursor = database.query(HabitDatabaseHelper.TABLE_HABITS,
                null, null, null, null, null, null);

        habits = parseCursor(cursor);
        cursor.close();
        return habits;
    }

    // Filter and sort habits by name with optional filter and sort criteria
    public List<Habit> filterAndSortHabits(String filter, String sort) {
        List<Habit> habits = new ArrayList<>();
        String selection = filter != null ? HabitDatabaseHelper.COLUMN_HABIT_NAME + " LIKE ?" : null;
        String[] selectionArgs = filter != null ? new String[]{"%" + filter + "%"} : null;
        String orderBy = sort != null ? sort + " ASC" : HabitDatabaseHelper.COLUMN_HABIT_NAME;

        Cursor cursor = database.query(HabitDatabaseHelper.TABLE_HABITS,
                null, selection, selectionArgs, null, null, orderBy);

        habits = parseCursor(cursor);
        cursor.close();
        return habits;
    }

    // Search habits created between two timestamps
    public List<Habit> searchHabitsByTime(long fromTimestamp, long toTimestamp) {
        List<Habit> habits = new ArrayList<>();
        String selection = HabitDatabaseHelper.COLUMN_TIMESTAMP + " BETWEEN ? AND ?";
        String[] selectionArgs = {String.valueOf(fromTimestamp), String.valueOf(toTimestamp)};

        Cursor cursor = database.query(HabitDatabaseHelper.TABLE_HABITS,
                null, selection, selectionArgs, null, null, null);

        habits = parseCursor(cursor);
        cursor.close();
        return habits;
    }

    // Helper method to parse cursor and populate Habit objects
    private List<Habit> parseCursor(Cursor cursor) {
        List<Habit> habits = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
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
