package com.cityproject.model.aspects;


/**
 * Aspect for buildings that consume energy.
 * Implemented by: Residential, Commercial, Industrial, and some special buildings like FireStation and HealthBuilding.
 */
public interface HasEnergyConsumption {
    int getEnergyConsumption(); // energy units consumed each tick
}
