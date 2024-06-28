package com.example.hingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Adapter-Klasse für die Anzeige von Habit-Objekten in einer ListView
public class HabitAdapter extends ArrayAdapter<Habit> {

    private List<Habit> habits;
    private boolean isAscendingOrder = true; // Anfängliche Sortierreihenfolge

    // Konstruktor für den HabitAdapter
    public HabitAdapter(Context context, List<Habit> habits) {
        super(context, 0, habits);
        this.habits = habits != null ? habits : new ArrayList<>();
    }

    // Methode zur Erstellung und Rückgabe der View für ein einzelnes Listenelement
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.habit_item, parent, false);
        }

        Habit habit = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.habitName);
        nameTextView.setText(habit.getName());

        TextView timestampTextView = convertView.findViewById(R.id.timestamp);
        String formattedTimestamp = formatDate(habit.getTimestamp());
        timestampTextView.setText(formattedTimestamp); // Formatierte Zeitstempel-Text setzen

        return convertView;
    }

    // Hilfsmethode zur Formatierung des Zeitstempels
    private String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
        return sdf.format(date);
    }

    // Methode zum Hinzufügen einer Sammlung von Habits
    public void addAll(List<Habit> collection) {
        habits.clear();
        habits.addAll(collection);
        super.addAll(collection);
    }

    // Methode zum Löschen aller Habits
    @Override
    public void clear() {
        habits.clear();
        super.clear();
    }

    // Methode zum Sortieren der Habits nach Zeitstempel
    public void sortByTimestamp(boolean ascending) {
        isAscendingOrder = ascending;
        Collections.sort(habits, new Comparator<Habit>() {
            @Override
            public int compare(Habit habit1, Habit habit2) {
                long timestamp1 = habit1.getTimestamp();
                long timestamp2 = habit2.getTimestamp();
                if (isAscendingOrder) {
                    return Long.compare(timestamp1, timestamp2);
                } else {
                    return Long.compare(timestamp2, timestamp1);
                }
            }
        });
        notifyDataSetChanged();
    }
}