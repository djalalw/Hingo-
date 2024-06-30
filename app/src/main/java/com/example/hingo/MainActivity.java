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

        // Initialisierung von HabitDao und Öffnen der Datenbank
        habitDao = new HabitDao(this);
        habitDao.open();

        // Initialisierung der ListView und des HabitAdapters
        listView = findViewById(R.id.listView);
        habitAdapter = new HabitAdapter(this, new ArrayList<>());
        listView.setAdapter(habitAdapter);

        // Initialisierung der Buttons und EditText-Felder
        Button addButton = findViewById(R.id.addButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button searchButton = findViewById(R.id.searchButton);
        Button sortButton = findViewById(R.id.sortButton);
        EditText habitEditText = findViewById(R.id.habitEditText);
        EditText searchEditText = findViewById(R.id.searchEditText);

        // Listener für den Hinzufügen-Button
        addButton.setOnClickListener(v -> {
            String habitName = habitEditText.getText().toString().trim();
            if (!habitName.isEmpty()) {
                long timestamp = System.currentTimeMillis();
                Habit newHabit = new Habit(0, habitName, timestamp);
                long newId = habitDao.addHabit(newHabit);
                Log.d("MainActivity", "Neue Gewohnheit hinzugefügt mit ID: " + newId);
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

        // Listener für den Löschen-Button
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

        // Listener für den Suchen-Button
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            List<Habit> results = habitDao.searchHabits(query);
            habitAdapter.setHabits(results);
            Log.d("MainActivity", "Suchergebnisse Anzahl: " + results.size());
        });

        // Listener für den Sortieren-Button
        sortButton.setOnClickListener(v -> {
            isAscendingOrder = !isAscendingOrder;
            updateHabitList();
        });

        // Initiale Aktualisierung der Habits-Liste
        updateHabitList();
    }

    // Methode zur Aktualisierung der Habits-Liste
    private void updateHabitList() {
        List<Habit> allHabits = habitDao.getAllHabits();
        habitAdapter.setHabits(allHabits);
        habitAdapter.sort(isAscendingOrder);
        Log.d("MainActivity", "Habits aktualisiert, Anzahl: " + allHabits.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        habitDao.close(); // Schließen der Datenbank bei Zerstörung der Aktivität
    }
}
