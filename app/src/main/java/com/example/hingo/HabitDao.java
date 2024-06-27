package com.example.hingo;

import android.annotation.SuppressLint;
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

    public void addHabit(Habit habit) {
        ContentValues values = new ContentValues();
        values.put(HabitDatabaseHelper.COLUMN_HABIT_NAME, habit.getName());
        database.insert(HabitDatabaseHelper.TABLE_HABITS, null, values);
    }

    public void deleteHabit(long id) {
        database.delete(HabitDatabaseHelper.TABLE_HABITS, HabitDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public List<Habit> searchHabits(String query) {
        List<Habit> habits = new ArrayList<>();
        Cursor cursor = database.query(HabitDatabaseHelper.TABLE_HABITS, null,
                HabitDatabaseHelper.COLUMN_HABIT_NAME + " LIKE ?", new String[]{"%" + query + "%"},
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(HabitDatabaseHelper.COLUMN_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(HabitDatabaseHelper.COLUMN_HABIT_NAME));
            habits.add(new Habit(id, name));
            cursor.moveToNext();
        }
        cursor.close();
        return habits;
    }

    public List<Habit> getAllHabits() {
        List<Habit> habits = new ArrayList<>();
        Cursor cursor = database.query(HabitDatabaseHelper.TABLE_HABITS, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(HabitDatabaseHelper.COLUMN_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(HabitDatabaseHelper.COLUMN_HABIT_NAME));
            habits.add(new Habit(id, name));
            cursor.moveToNext();
        }
        cursor.close();
        return habits;
    }

    public List<Habit> filterAndSortHabits(String filter, String sort) {
        List<Habit> habits = new ArrayList<>();
        String selection = filter != null ? HabitDatabaseHelper.COLUMN_HABIT_NAME + " LIKE ?" : null;
        String[] selectionArgs = filter != null ? new String[]{"%" + filter + "%"} : null;
        String orderBy = sort != null ? sort + " ASC" : HabitDatabaseHelper.COLUMN_HABIT_NAME;

        Cursor cursor = database.query(HabitDatabaseHelper.TABLE_HABITS, null, selection, selectionArgs, null, null, orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(HabitDatabaseHelper.COLUMN_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(HabitDatabaseHelper.COLUMN_HABIT_NAME));
            habits.add(new Habit(id, name));
            cursor.moveToNext();
        }
        cursor.close();
        return habits;
    }
}
