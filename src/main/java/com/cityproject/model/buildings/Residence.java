package com.cityproject.model.buildings;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasHousing;
import com.cityproject.model.aspects.HasIncome;

/**
 * Residential building. Houses people and generates rent income.
 * Local happiness and health are affected by pollution in the cell.
 */
public class Residence extends Infrastructure implements HasHousing, HasIncome {

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

    // --- Values ---
    private static int getBuildCost(Type t) {
        return switch (t) { case COTTAGE -> 500; case CONDO -> 1500; case SKYSCRAPER -> 50000; };
    }

    @Override public int getCapacity() {
        return switch (type) { case COTTAGE -> 5; case CONDO -> 50; case SKYSCRAPER -> 2500; };
    }
    @Override public int getIncome() {
        return getRent();
    }

    public int getRent() {
        return switch (type) { case COTTAGE -> 25; case CONDO -> 120; case SKYSCRAPER -> 5000; };
    }

    @Override
    public int getEnergyConsumption() {
        return switch (type) { case COTTAGE -> 10; case CONDO -> 25; case SKYSCRAPER -> 100; };
    }


    //todo LOGICA DA SPOSTARE
    @Override
    public void applyEffects(CityState city) {
        // if is not active, than happiness drops to 0 and health is halved
        if (!isActive()) {
            // Deactivated residences: happiness drops to 0, health halved
            this.localHappiness = 0;
            this.localHealth = this.localHealth / 2;
            return;
        }
        // TODO non va bene, in questo modo la popolazione cresce ogni tick (da fare con le liste)
        city.setPopulation(city.getPopulation() + getCapacity());
        city.setBudget(city.getBudget() + getIncome());

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