package com.cityproject.model.components;

public class HousingComponent implements Component {
    private int capacity;
    private int currentResidents;
    private double localHappiness;
    private double localHealth;

    public HousingComponent(int capacity) {
        this.capacity = capacity;
        this.currentResidents = 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentResidents() {
        return currentResidents;
    }

    public void setCurrentResidents(int currentResidents) {
        this.currentResidents = Math.max(0, Math.min(capacity, currentResidents));
    }

    // Ritorna true se il numero di residenti ha raggiunto la capacità massima (per un eventuale nuova funzionalità)
    public boolean isFull() {
        return currentResidents >= capacity;
    }

    public double getLocalHappiness() {
        return localHappiness;
    }

    public void setLocalHappiness(double localHappiness) {
        this.localHappiness = localHappiness;
    }

    public double getLocalHealth() {
        return localHealth;
    }

    public void setLocalHealth(double localHealth) {
        this.localHealth = localHealth;
    }
}
