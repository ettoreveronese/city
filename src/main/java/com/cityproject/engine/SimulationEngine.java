package com.cityproject.engine;


import com.cityproject.model.CityState;
import com.cityproject.model.policy.CityPolicy;

//orchestra tutta la logica di simulazione, delegando a manager specializzati per ogni step
/**
 * The core simulation engine. Executes one "tick" at a time.
 * Follows exactly the 6-step sequence defined in the bibbia.
 * GRASP Controller: coordinates all simulation logic without doing calculations itself.
 */
public class SimulationEngine {

    private final CityState city;
    private CityPolicy activePolicy;

    // Managers
    private final RoadConnectionManager rcManager;
    private final EnergyBalanceManager ebManager;
    private final PollutionManager pManager;
    private final HealthCapacityManager hcManager;
    private final FireProtectionManager fpManager;
    private final EntertainmentManager entManager;
    private final PopulationHappinessHealthManager hhManager;
    private final BudgetManager bManager;
    private final EventManager eManager;

    public SimulationEngine(CityState city, CityPolicy initialPolicy) {
        this.city = city;
        this.activePolicy = initialPolicy;

        this.rcManager = new RoadConnectionManager(city);
        this.ebManager = new EnergyBalanceManager(city);
        this.pManager = new PollutionManager(city, activePolicy);
        this.hcManager = new HealthCapacityManager(city);
        this.fpManager = new FireProtectionManager(city);
        this.entManager = new EntertainmentManager(city);
        this.hhManager = new PopulationHappinessHealthManager(city);
        this.bManager = new BudgetManager(city, activePolicy);
        this.eManager = new EventManager(city);
    }

    /** Swap the active policy at runtime — Strategy Pattern in action */
    // Context
    public void setPolicy(CityPolicy policy) { 
        this.activePolicy = policy; 
        this.pManager.setActivePolicy(policy);
        this.bManager.setActivePolicy(policy);
    }
    public CityPolicy getActivePolicy()       { return activePolicy; }

    /**
     * Advance the simulation by one tick.
     * Steps follow the bibbia exactly:
     * 1. Check road destruction
     * 2. Energy balance → deactivate buildings if deficit
     * 3. Apply pollution to cells
     * 4. Apply happiness/health (local + global)
     * 5. Income and maintenance
     * 6. Events (fire, covid, tower collapse)
     */
    public void tick() {
        city.incrementTick();

        rcManager.checkConnections();
        ebManager.balanceEnergy();
        pManager.applyPollution();
        hcManager.calculateHealthCapacity();
        fpManager.applyFireProtectionEffects();
        entManager.applyEntertainment();
        hhManager.applyPopulationHappinessHealth();
        bManager.processIncomeAndMaintenance();
        eManager.processEvents();

        // Notify all observers (Dashboard, Logger, etc.)
        city.notifyObservers();
    }

}

