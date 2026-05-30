package com.cityproject.engine;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.EnergyComponent;
import com.cityproject.model.type.BuildingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnergyBalanceManagerTest {

    private CityState city;
    private EnergyBalanceManager ebm;

    @BeforeEach
    public void setup() {
        city = new CityState(10, 10, 1000);
        ebm = new EnergyBalanceManager(city);
    }

    private Infrastructure addEnergyBuilding(String id, int consumption, int production) {
        BuildingType type = new BuildingType(id, id, 0);
        type.addBaseComponent(new EnergyComponent(consumption, production));
        Infrastructure b = new Infrastructure(java.util.UUID.randomUUID().toString(), type, 0, 0);
        b.addComponent(new EnergyComponent(consumption, production));
        b.setActive(true);
        city.addBuilding(b);
        return b;
    }

    @Test
    public void testEnergySurplusKeepsBuildingsActive() {
        Infrastructure powerPlant = addEnergyBuilding("WIND", 0, 100);
        Infrastructure cottage1 = addEnergyBuilding("COTTAGE", 20, 0);
        Infrastructure cottage2 = addEnergyBuilding("COTTAGE", 30, 0);

        ebm.balanceEnergy();

        assertEquals(100, city.getTotalEnergyProduced());
        assertEquals(50, city.getTotalEnergyConsumed());
        assertTrue(powerPlant.isActive());
        assertTrue(cottage1.isActive());
        assertTrue(cottage2.isActive());
    }

    @Test
    public void testEnergyDeficitDeactivatesBuildings() {
        Infrastructure powerPlant = addEnergyBuilding("WIND", 0, 50);
        // Total consumption = 80, Production = 50 -> Deficit = 30
        Infrastructure condo1 = addEnergyBuilding("CONDO", 40, 0);
        Infrastructure condo2 = addEnergyBuilding("CONDO", 40, 0);

        ebm.balanceEnergy();

        assertEquals(50, city.getTotalEnergyProduced());
        
        // At least one condo must be deactivated to resolve deficit
        boolean oneDeactivated = !condo1.isActive() || !condo2.isActive();
        assertTrue(oneDeactivated, "At least one condo should be deactivated due to energy deficit");
        
        // Consumed energy must be updated after deactivation
        assertTrue(city.getTotalEnergyConsumed() <= city.getTotalEnergyProduced(), 
            "Total consumed should be <= produced after balancing");
    }
}
