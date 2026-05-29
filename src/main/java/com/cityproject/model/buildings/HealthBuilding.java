package com.cityproject.model.buildings;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasEnergyConsumption;
import com.cityproject.model.aspects.HasMaintenance;

/**
 * Sanitary building. Increases global health each tick.
 */
public class HealthBuilding extends Infrastructure implements HasMaintenance, HasEnergyConsumption {

    public enum Type { CLINIC, HOSPITAL }

    private final Type type;

    public HealthBuilding(String id, int x, int y, Type type) {
        super(id, x, y, getBuildCost(type));
        this.type = type;
    }

    private static int getBuildCost(Type t) {
        return switch (t) { case CLINIC -> 2000; case HOSPITAL -> 8000; };
    }

    @Override public int getMaintenanceCost() {
        return switch (type) { case CLINIC -> 100; case HOSPITAL -> 400; };
    }
    @Override public int getEnergyConsumption() { return 20; }

    private static double getHealthBoost(Type t) {
        return switch (t) { case CLINIC -> 1.0; case HOSPITAL -> 3.0; };
    }
    @Override
    public void applyEffects(CityState city) {
        if (!isActive()) return;
        double healthBoost = getHealthBoost(type);
        city.setGlobalHealth(city.getGlobalHealth() + healthBoost);
    }

    public Type getType() { return type; }
}