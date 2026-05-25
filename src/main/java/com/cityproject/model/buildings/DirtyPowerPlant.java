package com.cityproject.model.buildings;

import com.cityproject.model.Cell;
import com.cityproject.model.CityState;
import com.cityproject.model.aspects.HasMaintenance;
import com.cityproject.model.aspects.HasPollution;

/**
 * Polluting energy source: coal, oil, incinerator.
 * Produces energy but spreads pollution to nearby cells.
 */
public class DirtyPowerPlant extends PowerPlant implements HasPollution, HasMaintenance {

    public enum Type { COAL, OIL, INCINERATOR }

    private final Type type;

    public DirtyPowerPlant(String id, int x, int y, Type type) {
        super(id, x, y, getBuildCost(type), getEnergy(type));
        this.type = type;
    }

    private static int getBuildCost(Type t) {
        return switch (t) {
            case COAL        -> 2000;
            case OIL         -> 4000;
            case INCINERATOR -> 3000;
        };
    }

    private static int getEnergy(Type t) {
        return switch (t) {
            case COAL        -> 150;
            case OIL         -> 200;
            case INCINERATOR -> 100;
        };
    }

    @Override
    public int getPollutionLevel() {
        return switch (type) {
            case COAL        -> 40;
            case OIL         -> 50;
            case INCINERATOR -> 30;
        };
    }

    @Override
    public int getRange() { return 3; }

    @Override
    public int getMaintenanceCost() {
        return switch (type) {
            case COAL        -> 100;
            case OIL         -> 150;
            case INCINERATOR -> 80;
        };
    }

    @Override
    public void applyEffects(CityState city) {
        if (!isActive()) return;
        // Spread pollution to cells within range
        for (int dx = -getRange(); dx <= getRange(); dx++) {
            for (int dy = -getRange(); dy <= getRange(); dy++) {
                int nx = getX() + dx;
                int ny = getY() + dy;
                if (city.isValid(nx, ny)) {
                    Cell cell = city.getCell(nx, ny);
                    int distance = Math.abs(dx) + Math.abs(dy);
                    int spread = getPollutionLevel() / (distance + 1);
                    cell.setPollution(cell.getPollution() + spread);
                }
            }
        }
    }

    public Type getType() { return type; }
}