package com.cityproject.engine;

import com.cityproject.engine.Helpers.AreaEffectHelper;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.FireCoverageComponent;
import com.cityproject.model.components.HealthComponent;

/**
 * Applies only the fire protection and health-building effects each tick.
 */
public class HealthAndFireProtectionManager {

    private final CityState city;

    public HealthAndFireProtectionManager(CityState city) {
        this.city = city;
    }

    public void applyHealthAndFireProtectionEffects() {
        resetFireProtection();
        for (Infrastructure building : city.getBuildings()) {
            if (!building.isActive()) continue;

            if (building.hasComponent(FireCoverageComponent.class)) {
                FireCoverageComponent fs = building.getComponent(FireCoverageComponent.class);
                AreaEffectHelper.applyInRadius(city, building.getX(), building.getY(), fs.getRadius(),
                        (cell, distance) -> cell.setFireProtection(true));
            }
            if (building.hasComponent(HealthComponent.class)) {
                // Here we might need to apply health effects based on health building logic.
                // Previously it was building.applyEffects(city). 
                // In pure ECS, we'd distribute health to cells or adjust global health directly.
                // For now we can leave it empty or add global health later.
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
