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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HabitAdapter extends ArrayAdapter<Habit> {

    private List<Habit> habits;

    public HabitAdapter(Context context, List<Habit> habits) {
        super(context, 0, habits);
        this.habits = new ArrayList<>(habits);
    }

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
        timestampTextView.setText(formattedTimestamp);

        return convertView;
    }

    private String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
        return sdf.format(date);
    }

    public void setHabits(List<Habit> habits) {
        this.habits.clear();
        this.habits.addAll(habits);
        clear();
        addAll(this.habits);
        notifyDataSetChanged();
        Log.d("HabitAdapter", "Habits set, count: " + this.habits.size());
    }

    public void sort(boolean ascending) {
        Collections.sort(habits, (h1, h2) -> {
            if (ascending) {
                return Long.compare(h1.getTimestamp(), h2.getTimestamp());
            } else {
                return Long.compare(h2.getTimestamp(), h1.getTimestamp());
            }
        });
        clear();
        addAll(habits);
        notifyDataSetChanged();
    }
}