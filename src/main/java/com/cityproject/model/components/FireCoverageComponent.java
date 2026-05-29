package com.cityproject.model.components;

public class FireCoverageComponent implements Component {
    private int radius;

    public FireCoverageComponent(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
