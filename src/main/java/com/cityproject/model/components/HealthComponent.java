package com.cityproject.model.components;

public class HealthComponent implements Component {
    private int capacity;

    public HealthComponent(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
