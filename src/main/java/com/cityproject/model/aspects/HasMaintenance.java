package com.cityproject.model.aspects;

/**
 * Aspect for buildings that have a maintenance cost each tick.
 * Implemented by: CleanPowerPlant, FireStation, NaturalArea, HealthBuilding
 */
public interface HasMaintenance {
    int getMaintenanceCost(); // money subtracted from budget each tick
}