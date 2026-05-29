package com.cityproject.model.components;

public class HealthComponent implements Component {
    // Represents the health capacity of a residential building. Higher pollution reduces this capacity.
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
