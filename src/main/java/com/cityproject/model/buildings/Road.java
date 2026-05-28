package com.cityproject.model.buildings;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasEnergyConsumption;
import com.cityproject.model.aspects.HasMaintenance;

/**
 * Road segment. Used by the VCS (Road Connection Verification) algorithm.
 * Buildings are only active if connected to the root road via roads.
 * The root road (Strada-Radice) is indestructible and placed at map center.
 */
public class Road extends Infrastructure implements HasMaintenance, HasEnergyConsumption {

    private final boolean isRoot; // true only for Strada-Radice

    public Road(String id, int x, int y, boolean isRoot) {
        super(id, x, y, isRoot ? 0 : 100);
        this.isRoot = isRoot;
    }

    public boolean isRoot() { return isRoot; }

    @Override
    public int getMaintenanceCost() {
        return isRoot ? 0 : 5; // Root road has no maintenance cost
    }

    @Override public int getEnergyConsumption() { return 0; }

    @Override
    public void applyEffects(CityState city) {
        // Roads have no direct effects — their role is connectivity (VCS)
    }
}