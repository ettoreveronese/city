package com.cityproject.model.aspects;

/**
 * Aspect for buildings that produce energy.
 * Implemented by: CleanPowerPlant, DirtyPowerPlant (via PowerPlant)
 */
public interface HasEnergyProduction {
    int getEnergyProduced(); // energy units produced each tick
    
}