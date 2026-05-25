package com.cityproject.model.aspects;

/**
 * Aspect for buildings that spread pollution to nearby cells.
 * Implemented by: ProductionBuilding, DirtyPowerPlant
 */
public interface HasPollution {
    int getPollutionLevel(); // pollution value emitted each tick
    int getRange();          // how many cells away the pollution spreads
}