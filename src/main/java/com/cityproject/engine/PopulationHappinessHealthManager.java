package com.cityproject.engine;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.HousingComponent;

/**
 * Manages local/global happiness, health AND population each tick.
 */
public class PopulationHappinessHealthManager {

    private static final double BASE_HAPPINESS = 100.0;
    private static final double BASE_HEALTH    = 80.0;
    private static final double POLLUTION_HAPPINESS_PENALTY = 1.0;
    private static final double POLLUTION_HEALTH_PENALTY    = 0.5;

    private final CityState city;

    public PopulationHappinessHealthManager(CityState city) {
        this.city = city;
    }

    public void applyPopulationHappinessHealth() {
        updateLocalValues();
        updatePopulation();
        updateGlobalValues();
    }

    private void updateLocalValues() {
        for (Infrastructure b : city.getHousingBuildings()) {
            HousingComponent r = b.getComponent(HousingComponent.class);

            if (!b.isActive()) {
                r.setLocalHappiness(0);
                r.setLocalHealth(r.getLocalHealth() / 2.0);
                continue;
            }

            double happiness = BASE_HAPPINESS;
            double health    = BASE_HEALTH;

            int pollution = city.getCell(b.getX(), b.getY()).getPollution();
            happiness -= pollution * POLLUTION_HAPPINESS_PENALTY;
            health    -= pollution * POLLUTION_HEALTH_PENALTY;
            
            int entertainment = city.getCell(b.getX(), b.getY()).getEntertainmentBonus();
            happiness += entertainment;

            r.setLocalHappiness(happiness);
            r.setLocalHealth(health);
        }
    }

    private void updatePopulation() {
        int total = 0;
        for (Infrastructure b : city.getHousingBuildings()) {
            HousingComponent h = b.getComponent(HousingComponent.class);
            if (b.isActive()) total += h.getCapacity();
        }
        city.setPopulation(total);
    }

    private void updateGlobalValues() {
        double sumPH = 0, sumPS = 0, sumP = 0;
        for (Infrastructure b : city.getHousingBuildings()) {
            HousingComponent r = b.getComponent(HousingComponent.class);
            sumPH += (double) r.getCapacity() * r.getLocalHappiness();
            sumPS += (double) r.getCapacity() * r.getLocalHealth();
            sumP  += r.getCapacity();
        }
        if (sumP > 0) {
            city.setGlobalHappiness(sumPH / sumP);
            city.setGlobalHealth(sumPS / sumP);
        }
    }
}