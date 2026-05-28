package com.cityproject.model.buildings;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasEnergyConsumption;
import com.cityproject.model.aspects.HasIncome;
import com.cityproject.model.aspects.HasPollution;

/**
 * Industrial building. Generates income but spreads pollution.
 * FOOD = low pollution, METALLURGICAL = medium, PETROCHEMICAL = high.
 */
public class ProductionBuilding extends Infrastructure implements HasIncome, HasPollution, HasEnergyConsumption {

    public enum Type { FOOD, METALLURGICAL, PETROCHEMICAL }

    private final Type type;

    public ProductionBuilding(String id, int x, int y, Type type) {
        super(id, x, y, getBuildCost(type));
        this.type = type;
    }

    private static int getBuildCost(Type t) {
        return switch (t) { case FOOD -> 2000; case METALLURGICAL -> 5000; case PETROCHEMICAL -> 8000; };
    }

    @Override public int getIncome() {
        return switch (type) { case FOOD -> 300; case METALLURGICAL -> 600; case PETROCHEMICAL -> 1000; };
    }
    @Override public int getPollutionLevel() {
        return switch (type) { case FOOD -> 10; case METALLURGICAL -> 20; case PETROCHEMICAL -> 30; };
    }
    @Override public int getRange() {
        return switch (type) { case FOOD -> 2; case METALLURGICAL -> 3; case PETROCHEMICAL -> 4; };
    }

    // @Override public int getRange() { return 3; }
    @Override public int getEnergyConsumption() { return 50; }

    
    @Override
    public void applyEffects(CityState city) {
        /*
        if (!isActive()) return;

        // Add income to budget
        city.setBudget(city.getBudget() + getIncome());

        // Spread pollution to cells within range
        for (int dx = -getRange(); dx <= getRange(); dx++) {
            for (int dy = -getRange(); dy <= getRange(); dy++) {
                int nx = getX() + dx;
                int ny = getY() + dy;
                if (city.isValid(nx, ny)) {
                    Cell cell = city.getCell(nx, ny);
                    int distance = Math.abs(dx) + Math.abs(dy);
                    cell.setPollution(cell.getPollution() + getPollutionLevel() / (distance + 1));//
                }
            }
        }
            */
    }

    public Type getType() { return type; }
}