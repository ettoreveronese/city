package com.cityproject.model.buildings;

import com.cityproject.model.CityState;
import com.cityproject.model.aspects.HasMaintenance;

/**
 * Clean energy source: solar, wind, nuclear.
 * Produces energy and has a maintenance cost but no pollution.
 */
public class CleanPowerPlant extends PowerPlant implements HasMaintenance {

    public enum Type { SOLAR, WIND, NUCLEAR }

    private final Type type;

    public CleanPowerPlant(String id, int x, int y, Type type) {
        super(id, x, y, getBuildCost(type), getEnergy(type));
        this.type = type;
    }

    private static int getBuildCost(Type t) {
        return switch (t) {
            case SOLAR   -> 3000;
            case WIND    -> 2500;
            case NUCLEAR -> 15000;
        };
    }

    private static int getEnergy(Type t) {
        return switch (t) {
            case SOLAR   -> 20;   // from bibbia: fotovoltaico 20
            case WIND    -> 30;
            case NUCLEAR -> 250;  // from bibbia: nucleare 250
        };
    }

    @Override
    public int getMaintenanceCost() {
        return switch (type) {
            case SOLAR   -> 50;
            case WIND    -> 40;
            case NUCLEAR -> 300;
        };
    }

    @Override
    public void applyEffects(CityState city) {
        if (!isActive()) return;
        // Clean plants have no negative side effects — energy is handled by SimulationEngine
    }

    public Type getType() { return type; }
}