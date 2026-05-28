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

    public SimulationEngine(CityState city, CityPolicy initialPolicy) {
        this.city = city;
        this.activePolicy = initialPolicy;
    }

    /** Swap the active policy at runtime — Strategy Pattern in action */
    public void setPolicy(CityPolicy policy) { this.activePolicy = policy; }
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

        // delegate to managers
        RoadConnectionManager rcManager = new RoadConnectionManager(city);
        EnergyBalanceManager ebManager = new EnergyBalanceManager(city);
        PollutionManager pManager = new PollutionManager(city, activePolicy);
        HealthAndFireProtectionManager hfManager = new HealthAndFireProtectionManager(city);
        PopulationHappinessHealthManager hhManager = new PopulationHappinessHealthManager(city);
        BudgetManager bManager = new BudgetManager(city, activePolicy);
        EventManager eManager = new EventManager(city);

        rcManager.checkConnections();
        ebManager.balanceEnergy();
        pManager.applyPollution();
        hfManager.applyHealthAndFireProtectionEffects();
        hhManager.applyPopulationHappinessHealth();
        bManager.processIncomeAndMaintenance();
        eManager.processEvents();

        // Notify all observers (Dashboard, Logger, etc.)
        city.notifyObservers();
    }

}

