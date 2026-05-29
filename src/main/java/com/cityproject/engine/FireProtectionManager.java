package com.cityproject.engine;

import com.cityproject.engine.Helpers.AreaEffectHelper;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.FireCoverageComponent;

/**
 * Applies only the fire protection effects each tick.
 */
public class FireProtectionManager {

    private final CityState city;

    public FireProtectionManager(CityState city) {
        this.city = city;
    }

    public void applyFireProtectionEffects() {
        resetFireProtection();

        for (Infrastructure building : city.getBuildings()) {
            if (!building.isActive()) continue;

            // Apply fire protection coverage
            if (building.hasComponent(FireCoverageComponent.class)) {
                FireCoverageComponent fs = building.getComponent(FireCoverageComponent.class);
                AreaEffectHelper.applyInRadius(city, building.getX(), building.getY(), fs.getRadius(),
                        (cell, distance) -> cell.setFireProtection(true));
            }
        }
    }

    private void resetFireProtection() {
        for (int i = 0; i < city.getRows(); i++) {
            for (int j = 0; j < city.getCols(); j++) {
                city.getCell(i, j).setFireProtection(false);
            }
        }
    }
}
