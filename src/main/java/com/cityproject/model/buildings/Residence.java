package com.cityproject.model.buildings;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasHousing;

/**
 * Residential building. Houses people and generates rent income.
 * Local happiness and health are affected by pollution in the cell.
 */
public class Residence extends Infrastructure implements HasHousing {

    public enum Type { COTTAGE, CONDO, SKYSCRAPER }

    private final Type type;
    private double localHappiness;
    private double localHealth;

    public Residence(String id, int x, int y, Type type) {
        super(id, x, y, getWidth(type), getHeight(type), getBuildCost(type));
        this.type = type;
        this.localHappiness = 50.0;
        this.localHealth = 50.0;
    }

    // --- Values from bibbia ---
    private static int getBuildCost(Type t) {
        return switch (t) { case COTTAGE -> 500; case CONDO -> 1500; case SKYSCRAPER -> 50000; };
    }
    private static int getWidth(Type t) {
        return switch (t) { case COTTAGE -> 1; case CONDO -> 2; case SKYSCRAPER -> 4; };
    }
    private static int getHeight(Type t) {
        return switch (t) { case COTTAGE -> 1; case CONDO -> 2; case SKYSCRAPER -> 4; };
    }

    @Override public int getCapacity() {
        return switch (type) { case COTTAGE -> 5; case CONDO -> 50; case SKYSCRAPER -> 2500; };
    }
    @Override public int getRent() {
        return switch (type) { case COTTAGE -> 25; case CONDO -> 120; case SKYSCRAPER -> 5000; };
    }

    @Override
    public int getEnergyConsumption() {
        return switch (type) { case COTTAGE -> 10; case CONDO -> 25; case SKYSCRAPER -> 100; };
    }

    @Override
    public void applyEffects(CityState city) {
        if (!isActive()) {
            // Deactivated residences: happiness drops to 0, health halved
            this.localHappiness = 0;
            this.localHealth = this.localHealth / 2;
            return;
        }
        // Add residents and rent to city
        city.setPopulation(city.getPopulation() + getCapacity());
        city.setBudget(city.getBudget() + getRent());

        // Pollution in this cell reduces local happiness and health (bibbia formula)
        int cellPollution = city.getCell(getX(), getY()).getPollution();
        this.localHappiness = Math.max(0, localHappiness - cellPollution);
        this.localHealth    = Math.max(0, localHealth - cellPollution / 2.0);
    }

    public Type getType()                   { return type; }
    public double getLocalHappiness()       { return localHappiness; }
    public void setLocalHappiness(double h) { this.localHappiness = Math.max(0, Math.min(100, h)); }
    public double getLocalHealth()          { return localHealth; }
    public void setLocalHealth(double h)    { this.localHealth = Math.max(0, Math.min(100, h)); }
}