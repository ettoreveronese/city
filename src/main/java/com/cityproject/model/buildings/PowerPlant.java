package com.cityproject.model.buildings;

import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasEnergy;

/**
 * Abstract base for all power plants (clean and dirty).
 * GRASP High Cohesion: groups all energy-production logic in one place.
 * SimulationEngine can iterate List<PowerPlant> to compute total energy balance.
 */
public abstract class PowerPlant extends Infrastructure implements HasEnergy {

    private final int energyProduced;

    public PowerPlant(String id, int x, int y, int buildCost, int energyProduced) {
        super(id, x, y, buildCost);
        this.energyProduced = energyProduced;
    }
    

    @Override
    public int getEnergyProduced() { return energyProduced; }

    // Power plants consume no energy — they produce it
    @Override
    public int getEnergyConsumption() { return 0; }
}