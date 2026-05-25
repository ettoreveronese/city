package com.cityproject.engine;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.buildings.Residence;

/**
 * Manages happiness and health calculations.
 * Global happiness/health computed as weighted average over residences.
 */
public class HappinessHealthManager {

    private final CityState city;

    public HappinessHealthManager(CityState city) {
        this.city = city;
    }

    public void applyHappinessAndHealth() {
        // Reset fire protection each tick
        for (int i = 0; i < city.getRows(); i++)
            for (int j = 0; j < city.getCols(); j++)
                city.getCell(i, j).setFireProtection(false);

        // Apply all building effects (residences update local happiness/health, etc.)
        for (Infrastructure b : city.getBuildings())
            b.applyEffects(city);

        // Compute global happiness as weighted average over residences
        // globalHappiness = Σ(pi * fi) / Σpi
        double sumPF = 0, sumPS = 0, sumP = 0;
        for (Infrastructure b : city.getBuildings()) {
            if (b instanceof Residence r && r.isActive()) {
                sumPF += r.getCapacity() * r.getLocalHappiness();
                sumPS += r.getCapacity() * r.getLocalHealth();
                sumP  += r.getCapacity();
            }
        }
        if (sumP > 0) {
            city.setGlobalHappiness(sumPF / sumP);
            city.setGlobalHealth(sumPS / sumP);
        }
    }
}
