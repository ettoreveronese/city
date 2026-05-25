package com.cityproject.model.buildings;

import com.cityproject.model.Cell;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasMaintenance;

/**
 * Parks and natural reserves. Reduce pollution in nearby cells
 * and increase global happiness.
 */
public class NaturalArea extends Infrastructure implements HasMaintenance {

    public enum Type { PARK, NATURE_RESERVE, NATIONAL_PARK }

    private final Type type;

    public NaturalArea(String id, int x, int y, Type type) {
        super(id, x, y, getWidth(type), getHeight(type), getBuildCost(type));
        this.type = type;
    }

    private static int getBuildCost(Type t) {
        return switch (t) { case PARK -> 500; case NATURE_RESERVE -> 2000; case NATIONAL_PARK -> 5000; };
    }
    private static int getWidth(Type t) {
        return switch (t) { case PARK -> 1; case NATURE_RESERVE -> 2; case NATIONAL_PARK -> 4; };
    }
    private static int getHeight(Type t) {
        return switch (t) { case PARK -> 1; case NATURE_RESERVE -> 2; case NATIONAL_PARK -> 4; };
    }

    public int getRadius() {
        return switch (type) { case PARK -> 2; case NATURE_RESERVE -> 4; case NATIONAL_PARK -> 6; };
    }

    @Override public int getMaintenanceCost() {
        return switch (type) { case PARK -> 50; case NATURE_RESERVE -> 150; case NATIONAL_PARK -> 400; };
    }
    @Override public int getEnergyConsumption() { return 0; }

    @Override
    public void applyEffects(CityState city) {
        if (!isActive()) return;

        // Reduce pollution in cells within radius
        for (int dx = -getRadius(); dx <= getRadius(); dx++) {
            for (int dy = -getRadius(); dy <= getRadius(); dy++) {
                int nx = getX() + dx;
                int ny = getY() + dy;
                if (city.isValid(nx, ny)) {
                    Cell cell = city.getCell(nx, ny);
                    cell.setPollution(Math.max(0, cell.getPollution() - 5));
                }
            }
        }

        // Increase global happiness
        city.setGlobalHappiness(city.getGlobalHappiness() + 1.0);
    }

    public Type getType() { return type; }
}