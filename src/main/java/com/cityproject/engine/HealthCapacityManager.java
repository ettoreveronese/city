package com.cityproject.engine;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.HealthComponent;

/**
 * Calculates the total health capacity provided by medical structures each tick.
 */
public class HealthCapacityManager {

    private final CityState city;

    public HealthCapacityManager(CityState city) {
        this.city = city;
    }

    public void calculateHealthCapacity() {
        int totalHealthCapacity = 0;

        for (Infrastructure building : city.getHealthBuildings()) {
            if (!building.isActive()) continue;

            // Sum health capacity for population health calculations
            HealthComponent hc = building.getComponent(HealthComponent.class);
            totalHealthCapacity += hc.getCapacity();
        }
        
        city.setTotalHealthCapacity(totalHealthCapacity);
    }
}
