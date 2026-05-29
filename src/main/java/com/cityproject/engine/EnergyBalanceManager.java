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
        int totalConsumed = 0;

        // Sum produced and consumed
        for (Infrastructure infra : city.getEnergyBuildings()) {
            if (!infra.isActive()) continue;
            EnergyComponent ec = infra.getComponent(EnergyComponent.class);
            totalProduced += ec.getProduction();
            totalConsumed += ec.getConsumption();
        }

        // If deficit: randomly deactivate non-road buildings until balanced
        if (totalConsumed > totalProduced) {
            int deficit = totalConsumed - totalProduced;
            List<Infrastructure> candidates = new ArrayList<>();

            for (Infrastructure infra : city.getEnergyBuildings()) {
                if (!infra.isActive()) continue;
                // Strade let's assume they don't consume energy or are identified by type. 
                // For now just skip if it produces energy.
                EnergyComponent ec = infra.getComponent(EnergyComponent.class);
                if (ec.getProduction() > 0) continue; // don't deactivate producers
                if (ec.getConsumption() > 0) {
                    // Check if it's a road (based on type name in the future, skipping the check for now as roads don't have energy components)
                    if (infra.getType() != null && "ROAD".equals(infra.getType().getId())) continue;
                    if (infra.getType() != null && "ROOT_ROAD".equals(infra.getType().getId())) continue;
                    candidates.add(infra);
                }
            }

            Collections.shuffle(candidates);
            for (Infrastructure infra : candidates) {
                if (deficit <= 0) break;
                deficit -= infra.getComponent(EnergyComponent.class).getConsumption();
                infra.setActive(false);
                totalConsumed -= infra.getComponent(EnergyComponent.class).getConsumption(); // update consumed after deactivation
            }
        }
        
        city.setTotalEnergyProduced(totalProduced);
        city.setTotalEnergyConsumed(totalConsumed);
    }
}
