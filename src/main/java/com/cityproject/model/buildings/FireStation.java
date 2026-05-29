package com.cityproject.model.buildings;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasEnergyConsumption;
import com.cityproject.model.aspects.HasFireCoverage;
import com.cityproject.model.aspects.HasMaintenance;

/**
 * Provides fire protection to cells within its radius.
 * Protected cells survive fire events without being destroyed.
 */
public class FireStation extends Infrastructure implements HasFireCoverage, HasMaintenance, HasEnergyConsumption {

    public FireStation(String id, int x, int y) {
        super(id, x, y, 1500);
    }

    @Override public int getFireRadius()      { return 4; }
    @Override public int getMaintenanceCost() { return 150; }
    @Override public int getEnergyConsumption() { return 10; }

    @Override
    public void applyEffects(CityState city) {}
}