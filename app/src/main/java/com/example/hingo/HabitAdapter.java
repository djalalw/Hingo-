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

public class HabitAdapter extends ArrayAdapter<Habit> {

    private List<Habit> habits;
    private boolean isAscendingOrder = true; // Initial sort order

    public HabitAdapter(Context context, List<Habit> habits) {
        super(context, 0, habits);
        this.habits = habits != null ? habits : new ArrayList<>();
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
        timestampTextView.setText(formattedTimestamp); // Set formatted timestamp text

        return convertView;
    }

    private String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    public void addAll(List<Habit> collection) {
        habits.clear();
        habits.addAll(collection);
        super.addAll(collection);
    }

    @Override
    public void clear() {
        habits.clear();
        super.clear();
    }

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
