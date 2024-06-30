package com.example.hingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HabitAdapter extends ArrayAdapter<Habit> {

    // Liste zum Speichern von Habit-Objekten
    private List<Habit> habits;

    // Konstruktor für den HabitAdapter
    public HabitAdapter(Context context, List<Habit> habits) {
        super(context, 0, habits);
        this.habits = new ArrayList<>(habits); // Initialisieren der Habits-Liste
    }

    // Methode zum Erstellen und Zurückgeben einer Ansicht für jedes Element in der Liste
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Ansicht aufblähen, wenn sie noch nicht erstellt wurde
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.habit_item, parent, false);
        }

        // Habit-Objekt an der aktuellen Position abrufen
        Habit habit = getItem(position);

        // TextView für den Namen des Habits finden und setzen
        TextView nameTextView = convertView.findViewById(R.id.habitName);
        nameTextView.setText(habit.getName());

        // TextView für den formatierten Zeitstempel finden und setzen
        TextView timestampTextView = convertView.findViewById(R.id.timestamp);
        String formattedTimestamp = formatDate(habit.getTimestamp());
        timestampTextView.setText(formattedTimestamp);

        return convertView; // Zurückgeben der vollständigen Ansicht zum Rendern auf dem Bildschirm
    }

    // Methode zum Formatieren des Zeitstempels in einen lesbaren Datumsstring
    private String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
        return sdf.format(date);
    }

    // Methode zum Setzen der Liste von Habits und Benachrichtigen des Adapters über Datenänderungen
    public void setHabits(List<Habit> habits) {
        this.habits.clear(); // Aktuelle Liste löschen
        this.habits.addAll(habits); // Alle neuen Habits hinzufügen
        clear();
        addAll(this.habits); // Aktualisierte Liste zum Adapter hinzufügen
        notifyDataSetChanged(); // Benachrichtigen, dass sich die Daten geändert haben
        Log.d("HabitAdapter", "Habits gesetzt, Anzahl: " + this.habits.size()); // Neue Habit-Anzahl protokollieren
    }

    // Methode zum Sortieren der Habits nach Zeitstempel
    public void sort(boolean ascending) {
        Collections.sort(habits, (h1, h2) -> {
            if (ascending) {
                return Long.compare(h1.getTimestamp(), h2.getTimestamp()); // Aufsteigend sortieren
            } else {
                return Long.compare(h2.getTimestamp(), h1.getTimestamp()); // Absteigend sortieren
            }
        });
        clear();
        addAll(habits); // Adapter mit der sortierten Liste aktualisieren
        notifyDataSetChanged(); // Benachrichtigen, dass sich die Daten geändert haben
    }
}
