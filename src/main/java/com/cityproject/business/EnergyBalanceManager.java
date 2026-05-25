package com.cityproject.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasEnergy;
import com.cityproject.model.buildings.Road;

/**
 * Manages energy production, consumption, and balancing.
 * If deficit: randomly deactivate non-road buildings until balanced.
 */
public class EnergyBalanceManager {

    private final CityState city;

    public EnergyBalanceManager(CityState city) {
        this.city = city;
    }

    public void balanceEnergy() {
        int totalProduced = 0;
        int totalConsumed = 0;

        for (Infrastructure b : city.getBuildings()) {
            if (!b.isActive()) continue;
            if (b instanceof HasEnergy e) totalProduced += e.getEnergyProduced();
            totalConsumed += b.getEnergyConsumption();
        }

        // If deficit: randomly deactivate non-road buildings until balanced
        if (totalConsumed > totalProduced) {
            int deficit = totalConsumed - totalProduced;
            List<Infrastructure> candidates = new ArrayList<>();
            for (Infrastructure b : city.getBuildings())
                //non deve disattivare strutture gia disattivate, strade o centrali elettriche
                if (b.isActive() && !(b instanceof Road) && !(b instanceof HasEnergy)) candidates.add(b);

            Collections.shuffle(candidates);
            for (Infrastructure b : candidates) {
                // se non è più in deficit, esce dal ciclo
                if (deficit <= 0) break;
                // Deactivate building and reduce deficit by its consumption
                deficit -= b.getEnergyConsumption();
                
                b.setActive(false);
            }
        }
    }
}
