package com.cityproject.model.components;

public class PollutionComponent implements Component {
    private int pollutionGenerated;
    private int radius;

    public PollutionComponent(int pollutionGenerated, int radius) {
        this.pollutionGenerated = pollutionGenerated;
        this.radius = radius;
    }

    public int getPollutionGenerated() {
        return pollutionGenerated;
    }

    public void setPollutionGenerated(int pollutionGenerated) {
        this.pollutionGenerated = pollutionGenerated;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
