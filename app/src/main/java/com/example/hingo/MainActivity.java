package com.example.hingo;

import android.os.Bundle;
import android.util.Log;
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
    private boolean isAscendingOrder = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habitDao = new HabitDao(this);
        habitDao.open();

        listView = findViewById(R.id.listView);
        habitAdapter = new HabitAdapter(this, new ArrayList<>());
        listView.setAdapter(habitAdapter);

        Button addButton = findViewById(R.id.addButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button searchButton = findViewById(R.id.searchButton);
        Button sortButton = findViewById(R.id.sortButton);
        EditText habitEditText = findViewById(R.id.habitEditText);
        EditText searchEditText = findViewById(R.id.searchEditText);

        addButton.setOnClickListener(v -> {
            String habitName = habitEditText.getText().toString().trim();
            if (!habitName.isEmpty()) {
                long timestamp = System.currentTimeMillis();
                Habit newHabit = new Habit(0, habitName, timestamp);
                long newId = habitDao.addHabit(newHabit);
                Log.d("MainActivity", "New habit added with ID: " + newId);
                if (newId != -1) {
                    updateHabitList();
                    habitEditText.setText("");
                } else {
                    Toast.makeText(this, "Fehler beim Hinzufügen der Gewohnheit", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Gewohnheitsname darf nicht leer sein", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            String habitName = habitEditText.getText().toString().trim();
            if (!habitName.isEmpty()) {
                int deletedRows = habitDao.deleteHabitByName(habitName);
                if (deletedRows > 0) {
                    updateHabitList();
                    habitEditText.setText("");
                    Toast.makeText(this, "Gewohnheit gelöscht", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Gewohnheit nicht gefunden", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Geben Sie einen Gewohnheitsnamen zum Löschen ein", Toast.LENGTH_SHORT).show();
            }
        });

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            List<Habit> results = habitDao.searchHabits(query);
            habitAdapter.setHabits(results);
            Log.d("MainActivity", "Search results count: " + results.size());
        });

        sortButton.setOnClickListener(v -> {
            isAscendingOrder = !isAscendingOrder;
            updateHabitList();
        });

        updateHabitList();
    }

    private void updateHabitList() {
        List<Habit> allHabits = habitDao.getAllHabits();
        habitAdapter.setHabits(allHabits);
        habitAdapter.sort(isAscendingOrder);
        Log.d("MainActivity", "Habits updated, count: " + allHabits.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        habitDao.close();
    }
}