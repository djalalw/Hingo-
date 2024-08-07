package com.example.hingo;

// Klasse zur Repräsentation einer Gewohnheit
public class Habit {
    private long id;
    private String name;
    private long timestamp; // Zeitstempel für die Erstellung oder letzte Aktualisierung

    // Konstruktor für die Habit-Klasse
    public Habit(long id, String name, long timestamp) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    // Getter und Setter Methoden

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}