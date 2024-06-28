package com.example.hingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class HabitAdapter extends ArrayAdapter<Habit> {

    private List<Habit> habits;

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

        return convertView;
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
}
