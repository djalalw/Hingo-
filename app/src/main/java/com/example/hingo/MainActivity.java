package com.example.hingo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HabitDao habitDao;
    private ListView listView;
    private HabitAdapter habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habitDao = new HabitDao(this);
        habitDao.open();

        listView = findViewById(R.id.listView);
        habitAdapter = new HabitAdapter(this, new ArrayList<>()); // Anpassung des Konstruktors
        listView.setAdapter(habitAdapter);

        Button addButton = findViewById(R.id.addButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button searchButton = findViewById(R.id.searchButton);
        EditText habitEditText = findViewById(R.id.habitEditText);
        EditText searchEditText = findViewById(R.id.searchEditText);

        addButton.setOnClickListener(v -> {
            String habitName = habitEditText.getText().toString().trim();
            if (!habitName.isEmpty()) {
                habitDao.addHabit(new Habit(0, habitName));
                updateHabitList();
                habitEditText.setText("");
            } else {
                Toast.makeText(this, "Habit name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            String habitName = habitEditText.getText().toString().trim();
            if (!habitName.isEmpty()) {
                Habit habit = null;
                for (Habit h : habitDao.searchHabits(habitName)) {
                    habit = h;
                    break;
                }
                if (habit != null) {
                    habitDao.deleteHabit(habit.getId());
                    updateHabitList();
                    habitEditText.setText("");
                } else {
                    Toast.makeText(this, "Habit not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter a habit name to delete", Toast.LENGTH_SHORT).show();
            }
        });

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            List<Habit> results = habitDao.searchHabits(query);
            habitAdapter.clear();
            habitAdapter.addAll(results);
            habitAdapter.notifyDataSetChanged();
        });

        updateHabitList();
    }

    private void updateHabitList() {
        List<Habit> allHabits = habitDao.getAllHabits();
        habitAdapter.clear();
        habitAdapter.addAll(allHabits);
        habitAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        habitDao.close();
    }
}
