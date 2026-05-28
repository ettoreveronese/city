package com.cityproject.engine;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasHousing;
import com.cityproject.model.buildings.Residence;

/**
 * Manages local/global happiness, health AND population each tick.
 * All three metrics derive from residences, so they live here together (GRASP High Cohesion).
 *
 * LOCAL happiness/health: computed per residence based on cell pollution.
 * GLOBAL happiness/health: weighted average over all residences (bibbia formula).
 * POPULATION: recomputed from scratch each tick — no accumulation bug.
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
        updateLocalValues();       // happiness + health per residence
        updatePopulation();        // population from housing list
        updateGlobalValues();      // weighted averages
    }

    private void updateLocalValues() {
        for (Infrastructure b : city.getBuildings()) {
            if (!(b instanceof Residence r)) continue;

            if (!r.isActive()) {
                // Bibbia: deactivated residence → happiness=0, health halved
                r.setLocalHappiness(0);
                r.setLocalHealth(r.getLocalHealth() / 2.0);
                continue;
            }

            // Start from base values each tick — avoids infinite decay
            double happiness = BASE_HAPPINESS;
            double health    = BASE_HEALTH;

            // Penalty from pollution in this cell
            int pollution = city.getCell(r.getX(), r.getY()).getPollution();
            happiness -= pollution * POLLUTION_HAPPINESS_PENALTY;
            health    -= pollution * POLLUTION_HEALTH_PENALTY;

            r.setLocalHappiness(happiness);
            r.setLocalHealth(health);
        }
    }

    //Gestione popolazione: ogni tick si ricomputa da zero in base alla capacità totale delle residenze attive, così non c'è rischio di bug di accumulo o decremento infinito.
    private void updatePopulation() {
        int total = 0;
        for (HasHousing h : city.getHousing()) {
            Infrastructure b = (Infrastructure) h;
            if (b.isActive()) total += h.getCapacity();
        }
        city.setPopulation(total);
    }

    private void updateGlobalValues() {
        // Bibbia formula: Σ(capacity_i * value_i) / Σ(capacity_i)
        double sumPH = 0, sumPS = 0, sumP = 0;
        for (Infrastructure b : city.getBuildings()) {
            if (!(b instanceof Residence r)) continue;
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