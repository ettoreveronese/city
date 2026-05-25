package com.cityproject.business;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasIncome;
import com.cityproject.model.aspects.HasMaintenance;
import com.cityproject.model.policy.CityPolicy;

/**
 * Manages income and maintenance costs using the aspect lists.
 * Income from HasIncome buildings, maintenance costs from HasMaintenance buildings.
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
        city.setBudget(city.getBudget() + activePolicy.calculateTax(city)); //aggiunge soldi in base alla politica attiva e al numero di abitanti
        activePolicy.applyPolicyEffects(city); //extra effectr from active policy //^per adesso disattivato

        // Calculate total income from all active income buildings
        int totalIncome = 0;
        for (HasIncome incomeBuilding : city.getIncomes()) {
            Infrastructure b = (Infrastructure) incomeBuilding;
            if (b.isActive()) {
                totalIncome += incomeBuilding.getIncome();
            }
        }
        city.setBudget(city.getBudget() + totalIncome);

        // Calculate total maintenance costs
        int totalMaintenance = 0;
        for (HasMaintenance maintBuilding : city.getMaintenance()) {
            Infrastructure b = (Infrastructure) maintBuilding;
            int cost = b.isActive() ? maintBuilding.getMaintenanceCost() : maintBuilding.getMaintenanceCost() * 2;
            totalMaintenance += cost;
        }
        city.setBudget(city.getBudget() - totalMaintenance);
    }
}
