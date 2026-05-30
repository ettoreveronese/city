package com.cityproject.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.EnergyComponent;

/**
 * Manages energy production, consumption, and balancing.
 * If deficit: randomly deactivate buildings until balanced.
 */
public class EnergyBalanceManager {

    private final CityState city;

    public EnergyBalanceManager(CityState city) {
        this.city = city;
    }

    public void balanceEnergy() {
        int totalProduced = 0;
        int demandOfActive = 0;
        int totalRequested = 0;

        // Sum produced and consumed
        for (Infrastructure infra : city.getEnergyBuildings()) {
            EnergyComponent ec = infra.getComponent(EnergyComponent.class);
            totalRequested += ec.getConsumption(); // total demand regardless of active status

            if (infra.isActive()) {
                totalProduced += ec.getProduction();
                demandOfActive += ec.getConsumption();
            }
        }

        // If deficit: randomly deactivate non-road buildings until balanced
        if (demandOfActive > totalProduced) {
            int deficit = demandOfActive - totalProduced;
            List<Infrastructure> candidates = new ArrayList<>();

            // Only consider active non-road energy consumers for deactivation
            for (Infrastructure infra : city.getEnergyBuildings()) {
                if (!infra.isActive()) continue;
                EnergyComponent ec = infra.getComponent(EnergyComponent.class);
                if (ec.getProduction() > 0) continue; // don't deactivate producers
                if (ec.getConsumption() > 0) {
                    if (infra.getType() != null && infra.getType().isRoad()) continue;
                    candidates.add(infra);
                }
            }

            Collections.shuffle(candidates);
            for (Infrastructure infra : candidates) {
                if (deficit <= 0) break;
                deficit -= infra.getComponent(EnergyComponent.class).getConsumption();
                infra.setActive(false);
            }
        }
        
        city.setTotalEnergyProduced(totalProduced);
        city.setTotalEnergyConsumed(totalRequested);
    }
}
