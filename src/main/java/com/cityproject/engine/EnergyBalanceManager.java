package com.cityproject.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasEnergyConsumption;
import com.cityproject.model.aspects.HasEnergyProduction;
import com.cityproject.model.buildings.Road;
//^Fatto


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

        // Sum produced from dedicated producers list
        for (HasEnergyProduction p : city.getEnergyProductions()) {
            Infrastructure infra = (Infrastructure) p;
            if (!infra.isActive()) continue;
            totalProduced += p.getEnergyProduced();
        }

        // Sum consumed from dedicated consumers list
        for (HasEnergyConsumption c : city.getEnergyConsumptions()) {
            Infrastructure infra = (Infrastructure) c;
            if (!infra.isActive()) continue;
            totalConsumed += c.getEnergyConsumption();
        }

        // If deficit: randomly deactivate non-road buildings until balanced
        if (totalConsumed > totalProduced) {
            int deficit = totalConsumed - totalProduced;
            List<Infrastructure> candidates = new ArrayList<>();

            // FIltro le strutture che posso disattivare (non strade, non produttori) tra quelle che consumano energia
            for (HasEnergyConsumption c : city.getEnergyConsumptions()) {
                Infrastructure infra = (Infrastructure) c;
                if (!infra.isActive()) continue;
                if (infra instanceof Road) continue;
                if (infra instanceof HasEnergyProduction) continue; // don't deactivate producers
                candidates.add(infra);
            }

            //Mescolo la lista per randomizzare l'ordine di disattivazione
            Collections.shuffle(candidates);
            for (Infrastructure infra : candidates) {
                if (deficit <= 0) break;
                deficit -= ((HasEnergyConsumption) infra).getEnergyConsumption();
                infra.setActive(false);
            }
        }
    }
}
