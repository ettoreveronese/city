package com.cityproject.model;

import java.util.ArrayList;
import java.util.List;

import com.cityproject.model.aspects.HasEnergyConsumption;
import com.cityproject.model.aspects.HasEnergyProduction;
import com.cityproject.model.aspects.HasHousing;
import com.cityproject.model.aspects.HasIncome;
import com.cityproject.model.aspects.HasMaintenance;
import com.cityproject.model.aspects.HasPollution;



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
    // permette alta coesione permettendo ad altre classi di specialiizzarsi in un aspetto specifico lavorando solo con dati che riguardano quell'aspetto
    // aumenta il low coupling permettendo alle altre classi di interagire solo con questa e non tra di loro, riducendo il rischio di bug e facilitando la manutenzione
    // aumenta la modularity permettendo di aggiungere nuovi aspetti semplicemente creando una nuova lista e aggiornando i metodi addBuilding e removeBuilding senza dover modificare le altre classi che si occupano degli aspetti specifici facilitando l'estendibilità del progetto
    private final List<Infrastructure> buildings = new ArrayList<>();
    private final List<HasHousing> housings = new ArrayList<>();
    private final List<HasIncome> incomes = new ArrayList<>();
    private final List<HasMaintenance> mantainances = new ArrayList<>();
    private final List<HasPollution> pollutions = new ArrayList<>();
    private final List<HasEnergyConsumption> energyConsumptions = new ArrayList<>();
    private final List<HasEnergyProduction> energyProductions = new ArrayList<>();


    // --- Observer Pattern: list of observers notified on every state change ---
    private final List<CityObserver> observers = new ArrayList<>();

    public CityState(int rows, int cols, int initialBudget) {
        this.rows = rows;
        this.cols = cols;
        this.budget = initialBudget;
        this.population = 0;
        this.globalHappiness = 80.0;
        this.globalHealth = 90.0;
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
    //verifica che sia dentro la mappa
    public boolean isValid(int x, int y)    { return x >= 0 && x < rows && y >= 0 && y < cols; }

    // --- Building management ---
    /*High Cohesion
        Queste liste permettono ai manager di lavorare solo con i dati rilevanti per il loro aspetto specifico

    */
    public void addBuilding(Infrastructure b){buildings.add(b);
        if (b instanceof HasHousing h) housings.add(h);
        if (b instanceof HasIncome i) incomes.add(i);
        if (b instanceof HasMaintenance m) mantainances.add(m);
        if (b instanceof HasPollution p) pollutions.add(p);
        if (b instanceof HasEnergyConsumption e) energyConsumptions.add(e);
        if (b instanceof HasEnergyProduction e) energyProductions.add(e);

        //stampo tutte le liste per vedere se funziona
        System.out.println("Buildings: " + buildings.size());
        System.out.println("Housings: " + housings.size());
        System.out.println("Incomes: " + incomes.size());
        System.out.println("Maintenance: " + mantainances.size());
        System.out.println("Pollutions: " + pollutions.size()+ "\n");
    }

    public void removeBuilding(Infrastructure b){
        buildings.remove(b);
        if (b instanceof HasHousing h) housings.remove(h);
        if (b instanceof HasIncome i) incomes.remove(i);
        if (b instanceof HasMaintenance m) mantainances.remove(m);
        if (b instanceof HasPollution p) pollutions.remove(p);
        if (b instanceof HasEnergyConsumption e) energyConsumptions.remove(e);
        if (b instanceof HasEnergyProduction e) energyProductions.remove(e);
        //stampo tutte le liste per vedere se funziona
        System.out.println("Buildings: " + buildings.size());
        System.out.println("Housings: " + housings.size());
        System.out.println("Incomes: " + incomes.size());
        System.out.println("Maintenance: " + mantainances.size());
        System.out.println("Pollutions: " + pollutions.size()+ "\n");
        System.out.println("EnergyConsumptions: " + energyConsumptions.size());
        System.out.println("EnergyProductions: " + energyProductions.size());
    }
    public List<Infrastructure> getBuildings()   { return buildings; }
    public List<HasHousing> getHousing()   { return housings; }
    public List<HasIncome> getIncomes()   { return incomes; }
    public List<HasMaintenance> getMaintenance()   { return mantainances; }
    public List<HasPollution> getPollutions()   { return pollutions; }
    public List<HasEnergyConsumption> getEnergyConsumptions()   { return energyConsumptions; }
    public List<HasEnergyProduction> getEnergyProductions()   { return energyProductions; }

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