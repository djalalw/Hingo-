package com.example.hingo;

import android.os.Bundle;
import java.util.ArrayList;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HabitDao habitDao;
    private ListView listView;
    private HabitAdapter habitAdapter;
    private boolean isAscendingOrder = true; // Anfängliche Sortierreihenfolge

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisierung der Datenbank-Zugriffs-Objekte
        habitDao = new HabitDao(this);
        habitDao.open();

        // Einrichten der ListView und des Adapters
        listView = findViewById(R.id.listView);
        habitAdapter = new HabitAdapter(this, new ArrayList<>()); // Adapter initialisieren
        listView.setAdapter(habitAdapter);

        // Referenzen auf UI-Elemente
        Button addButton = findViewById(R.id.addButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button searchButton = findViewById(R.id.searchButton);
        Button sortButton = findViewById(R.id.sortButton); // Neuer Sortier-Button
        EditText habitEditText = findViewById(R.id.habitEditText);
        EditText searchEditText = findViewById(R.id.searchEditText);

        // OnClickListener für den Hinzufügen-Button
        addButton.setOnClickListener(v -> {
            String habitName = habitEditText.getText().toString().trim();
            if (!habitName.isEmpty()) {
                long timestamp = System.currentTimeMillis(); // Aktuellen Zeitstempel abrufen
                Habit newHabit = new Habit(0, habitName, timestamp);
                habitDao.addHabit(newHabit);
                updateHabitList();
                habitEditText.setText("");
            } else {
                Toast.makeText(this, "Gewohnheitsname darf nicht leer sein", Toast.LENGTH_SHORT).show();
            }
        });

        // OnClickListener für den Löschen-Button
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
                    Toast.makeText(this, "Gewohnheit nicht gefunden", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Geben Sie einen Gewohnheitsnamen zum Löschen ein", Toast.LENGTH_SHORT).show();
            }
        });

        // OnClickListener für den Suchen-Button
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            List<Habit> results = habitDao.searchHabits(query);
            updateAdapterWithData(results);
        });

        // OnClickListener für den Sortieren-Button
        sortButton.setOnClickListener(v -> {
            isAscendingOrder = !isAscendingOrder; // Sortierreihenfolge umkehren
            updateHabitList(); // Liste neu sortieren und aktualisieren
        });

        updateHabitList(); // Anfängliche Anzeige der Gewohnheiten
    }

    // Methode zum Aktualisieren der Gewohnheitenliste
    private void updateHabitList() {
        List<Habit> allHabits = habitDao.getAllHabits();
        if (!isAscendingOrder) {
            Collections.reverse(allHabits); // Reihenfolge für absteigende Sortierung umkehren
        }
        updateAdapterWithData(allHabits);
    }

    // Methode zum Aktualisieren des Adapters mit neuen Daten
    private void updateAdapterWithData(List<Habit> habits) {
        habitAdapter.clear();
        habitAdapter.addAll(habits);
        habitAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        habitDao.close(); // Datenbank-Verbindung schließen
    }
}