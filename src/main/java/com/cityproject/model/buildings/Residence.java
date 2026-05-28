package com.cityproject.model.buildings;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasEnergyConsumption;
import com.cityproject.model.aspects.HasHousing;
import com.cityproject.model.aspects.HasIncome;

/**
 * Residential building — pure data container.
 * Local happiness and health are managed externally by HappinessHealthManager.
 * Population and budget are managed by BudgetManager.
 */
public class Residence extends Infrastructure implements HasHousing, HasIncome, HasEnergyConsumption {

    public enum Type { COTTAGE, CONDO, SKYSCRAPER }

    private final Type type;
    private double localHappiness;
    private double localHealth;

    public Residence(String id, int x, int y, Type type) {
        super(id, x, y, getBuildCost(type));
        this.type = type;
        this.localHappiness = 70.0;
        this.localHealth = 80.0;
    }

    private static int getBuildCost(Type t) {
        return switch (t) { case COTTAGE -> 500; case CONDO -> 1500; case SKYSCRAPER -> 50000; };
    }

    @Override public int getCapacity() {
        return switch (type) { case COTTAGE -> 5; case CONDO -> 50; case SKYSCRAPER -> 2500; };
    }

    @Override public int getIncome() { return getRent(); }

    @Override
    public int getRent() {
        return switch (type) { case COTTAGE -> 25; case CONDO -> 120; case SKYSCRAPER -> 5000; };
    }

    @Override
    public int getEnergyConsumption() {
        return switch (type) { case COTTAGE -> 10; case CONDO -> 25; case SKYSCRAPER -> 100; };
    }

    @Override
    public double getLocalHappiness()       { return localHappiness; }

    @Override
    public double getLocalHealth()          { return localHealth; }
 
    @Override
    public void applyEffects(CityState city) {}
    
    @Override
    public void setLocalHappiness(double h) { this.localHappiness = Math.max(0, Math.min(100, h)); }

    @Override
    public void setLocalHealth(double h)    { this.localHealth = Math.max(0, Math.min(100, h)); }

    public Type getType()                   { return type; }
}