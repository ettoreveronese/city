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
    private final int width;
    private final int height;
    private boolean active;
    private final int buildCost;

    public Infrastructure(String id, int x, int y, int width, int height, int buildCost) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
        this.buildCost = buildCost;
    }

    /**
     * How much energy this building consumes per tick.
     * Used by SimulationEngine to compute energy balance.
     */
    public abstract int getEnergyConsumption();

    /**
     * Apply this building's effects to the city state each tick.
     * Called by SimulationEngine during the tick cycle.
     */
    public abstract void applyEffects(CityState city);

    // --- Getters ---
    public String getId()       { return id; }
    public int getX()           { return x; }
    public int getY()           { return y; }
    public int getWidth()       { return width; }
    public int getHeight()      { return height; }
    public boolean isActive()   { return active; }
    public int getBuildCost()   { return buildCost; }
    public void setActive(boolean active) { this.active = active; }
}