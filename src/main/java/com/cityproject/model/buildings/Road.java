package com.cityproject.model.buildings;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;

/**
 * Road segment. Used by the VCS (Road Connection Verification) algorithm.
 * Buildings are only active if connected to the root road via roads.
 * The root road (Strada-Radice) is indestructible and placed at map center.
 */
public class Road extends Infrastructure {

    private final boolean isRoot; // true only for Strada-Radice

    public Road(String id, int x, int y, boolean isRoot) {
        super(id, x, y, 1, 1, isRoot ? 0 : 100);
        this.isRoot = isRoot;
    }

    public boolean isRoot() { return isRoot; }

    @Override public int getEnergyConsumption() { return 0; }

    @Override
    public void applyEffects(CityState city) {
        // Roads have no direct effects — their role is connectivity (VCS)
    }
}