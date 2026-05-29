package com.cityproject.engine;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.IncomeComponent;
import com.cityproject.model.components.MaintenanceComponent;
import com.cityproject.model.policy.CityPolicy;

/**
 * Manages income and maintenance costs using the component lists.
 */
public class BudgetManager {

    private final CityState city;
    private final CityPolicy activePolicy;

    public BudgetManager(CityState city, CityPolicy activePolicy) {
        this.city = city;
        this.activePolicy = activePolicy;
    }

    public void processIncomeAndMaintenance() {
        // Tax income from active policy
        city.setBudget(city.getBudget() + activePolicy.calculateTax(city));
        activePolicy.applyPolicyEffects(city);

        // Calculate total income from all active income buildings
        int totalIncome = 0;
        for (Infrastructure b : city.getIncomeBuildings()) {
            if (b.isActive()) {
                IncomeComponent inc = b.getComponent(IncomeComponent.class);
                totalIncome += inc.getBaseIncome();
            }
        }
        city.setBudget(city.getBudget() + totalIncome);

        // Calculate total maintenance costs
        int totalMaintenance = 0;
        for (Infrastructure b : city.getMaintenanceBuildings()) {
            MaintenanceComponent maint = b.getComponent(MaintenanceComponent.class);
            int cost = b.isActive() ? maint.getMaintenanceCost() : maint.getMaintenanceCost() * 2;
            totalMaintenance += cost;
        }
        city.setBudget(city.getBudget() - totalMaintenance);
    }
}
