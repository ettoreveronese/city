package com.cityproject.model.buildings;

import com.cityproject.model.CityState;
import com.cityproject.model.aspects.HasPollution;

/**
 * Polluting energy source: coal, oil, incinerator.
 * Produces energy but spreads pollution to nearby cells.
 */
public class DirtyPowerPlant extends PowerPlant implements HasPollution {

    public enum Type { INCINERATOR, COAL, OIL }

    private final Type type;

    public DirtyPowerPlant(String id, int x, int y, Type type) {
        super(id, x, y, getBuildCost(type), getEnergy(type));
        this.type = type;
    }

    private static int getBuildCost(Type t) {
        return switch (t) {
            case INCINERATOR -> 2000;
            case COAL        -> 3000;
            case OIL         -> 4000;
        };
    }

    private static int getEnergy(Type t) {
        return switch (t) {
            case INCINERATOR -> 100;
            case COAL        -> 150;
            case OIL         -> 200;
        };
    }

    @Override
    public int getPollutionLevel() {
        return switch (type) {
            case COAL        -> 40;
            case INCINERATOR -> 30;
            case OIL         -> 50;
        };
    }

    @Override
    public int getRange() {
        return switch (type) {
            case COAL        -> 2;
            case INCINERATOR -> 4;
            case OIL         -> 5;
        };
    }


    @Override
    public void applyEffects(CityState city) {}

    public Type getType() { return type; }
}