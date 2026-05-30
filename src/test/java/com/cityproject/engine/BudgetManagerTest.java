package com.cityproject.engine;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.IncomeComponent;
import com.cityproject.model.components.MaintenanceComponent;
import com.cityproject.model.policy.CityPolicy;
import com.cityproject.model.policy.GreenPolicy;
import com.cityproject.model.policy.IndustrialPolicy;
import com.cityproject.model.type.BuildingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetManagerTest {

    private CityState city;

    @BeforeEach
    public void setup() {
        city = new CityState(10, 10, 1000);
    }

    private Infrastructure addBudgetBuilding(String id, int income, int maintenance, boolean active) {
        BuildingType type = new BuildingType(id, id, 0);
        if (income > 0) type.addBaseComponent(new IncomeComponent(income));
        if (maintenance > 0) type.addBaseComponent(new MaintenanceComponent(maintenance));
        
        Infrastructure b = new Infrastructure(java.util.UUID.randomUUID().toString(), type, 0, 0);
        if (income > 0) b.addComponent(new IncomeComponent(income));
        if (maintenance > 0) b.addComponent(new MaintenanceComponent(maintenance));
        
        b.setActive(active);
        city.addBuilding(b);
        return b;
    }

    @Test
    public void testIncomeAndMaintenance() {
        // Green Policy: test with a neutral policy behavior for income
        BudgetManager bm = new BudgetManager(city, new CityPolicy() {
            public String getName() { return "Neutral"; }
            public int calculateTax(CityState c) { return 0; }
            public void applyPolicyEffects(CityState c) {}
            public double calculatePollutionModifier() { return 1.0; }
        });

        addBudgetBuilding("RESIDENTIAL", 50, 0, true);
        addBudgetBuilding("SERVICE", 0, 20, true);

        bm.processIncomeAndMaintenance();

        // Start budget: 1000 + 50 (income) - 20 (maintenance) = 1030
        assertEquals(1030, city.getBudget());
    }

    @Test
    public void testInactiveBuildingMaintenancePenalty() {
        BudgetManager bm = new BudgetManager(city, new GreenPolicy()); // Assuming GreenPolicy tax is minimal for empty city
        int initialBudget = city.getBudget();

        // Inactive buildings cost DOUBLE maintenance!
        addBudgetBuilding("SERVICE", 0, 20, false);

        int expectedTax = new GreenPolicy().calculateTax(city);

        bm.processIncomeAndMaintenance();

        // Budget: initial + tax - (20 * 2) = initial + tax - 40
        assertEquals(initialBudget + expectedTax - 40, city.getBudget());
    }

    @Test
    public void testIndustrialPolicyTaxBoost() {
        // Create some population to generate taxes
        city.setPopulation(100);

        BudgetManager bmGreen = new BudgetManager(city, new GreenPolicy());
        int taxGreen = new GreenPolicy().calculateTax(city);

        BudgetManager bmInd = new BudgetManager(city, new IndustrialPolicy());
        int taxInd = new IndustrialPolicy().calculateTax(city);

        // Industrial policy should give higher tax revenue than green policy for the same population
        assertTrue(taxInd > taxGreen, "Industrial policy should yield higher taxes than Green policy");
    }
}
