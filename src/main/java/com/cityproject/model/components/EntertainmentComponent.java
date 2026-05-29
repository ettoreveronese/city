package com.cityproject.model.components;

public class EntertainmentComponent implements Component {
    private int happinessBoost;
    private int radius;

    public EntertainmentComponent(int happinessBoost, int radius) {
        this.happinessBoost = happinessBoost;
        this.radius = radius;
    }

    public int getHappinessBoost() {
        return happinessBoost;
    }

    public void setHappinessBoost(int happinessBoost) {
        this.happinessBoost = happinessBoost;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
