package com.cityproject.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The central data store of the entire simulation.
 * GRASP Low Coupling: all modules read/write here without depending on each other.
 * Implements the Observable side of the Observer Pattern.
 */
public class CityState {

    // --- Grid ---
    private final int rows;
    private final int cols;
    private final Cell[][] grid;

    // --- Global city metrics ---
    private int budget;
    private int population;
    private double globalHappiness; // 0-100
    private double globalHealth;    // 0-100
    private int tick;

    // --- All placed buildings, kept in a flat list for easy iteration ---
    private final List<Infrastructure> buildings = new ArrayList<>();

    // --- Observer Pattern: list of observers notified on every state change ---
    private final List<CityObserver> observers = new ArrayList<>();

    public CityState(int rows, int cols, int initialBudget) {
        this.rows = rows;
        this.cols = cols;
        this.budget = initialBudget;
        this.population = 0;
        this.globalHappiness = 50.0;
        this.globalHealth = 50.0;
        this.tick = 0;

        // Initialize grid with empty cells
        this.grid = new Cell[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = new Cell(i, j);
    }

    // --- Observer Pattern ---
    public void addObserver(CityObserver o)    { observers.add(o); }
    public void removeObserver(CityObserver o) { observers.remove(o); }

    /** Notify all observers that the state has changed. Called after each tick. */
    public void notifyObservers() {
        for (CityObserver o : observers) o.onCityUpdated(this);
    }

    // --- Grid utilities ---
    public Cell getCell(int x, int y)       { return grid[x][y]; }
    public boolean isValid(int x, int y)    { return x >= 0 && x < rows && y >= 0 && y < cols; }

    // --- Building management ---
    public void addBuilding(Infrastructure b)    { buildings.add(b); }
    public void removeBuilding(Infrastructure b) { buildings.remove(b); }
    public List<Infrastructure> getBuildings()   { return buildings; }

    // --- Getters and setters ---
    public int getRows()            { return rows; }
    public int getCols()            { return cols; }
    public Cell[][] getGrid()       { return grid; }
    public int getBudget()          { return budget; }
    public void setBudget(int b)    { this.budget = b; }
    public int getPopulation()      { return population; }
    public void setPopulation(int p){ this.population = p; }
    public double getGlobalHappiness()          { return globalHappiness; }
    public void setGlobalHappiness(double h)    { this.globalHappiness = Math.max(0, Math.min(100, h)); }
    public double getGlobalHealth()             { return globalHealth; }
    public void setGlobalHealth(double h)       { this.globalHealth = Math.max(0, Math.min(100, h)); }
    public int getTick()            { return tick; }
    public void incrementTick()     { this.tick++; }
}