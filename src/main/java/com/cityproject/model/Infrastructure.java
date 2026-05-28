package com.cityproject.model;

/**
 * Abstract base class for ALL buildings and infrastructure in the city.
 * GRASP Information Expert: each building knows its own data.
 * Every concrete subclass must implement applyEffects() and getEnergyConsumption().
 */
public abstract class Infrastructure {

    private final String id;
    private final int x;
    private final int y;
    private boolean active;
    private final int buildCost;

    public Infrastructure(String id, int x, int y, int buildCost) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.active = true;
        this.buildCost = buildCost;
    }

    /**
     * Apply this building's effects to the city state each tick.
     * Called by SimulationEngine during the tick cycle.
     */
    public abstract void applyEffects(CityState city);

    // --- Getters ---
    public String getId()       { return id; }
    public int getX()           { return x; }
    public int getY()           { return y; }
    public boolean isActive()   { return active; }
    public int getBuildCost()   { return buildCost; }
    public void setActive(boolean active) { this.active = active; }
}