package com.cityproject.engine;

import com.cityproject.engine.Helpers.AreaEffectHelper;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasFireCoverage;
import com.cityproject.model.buildings.FireStation;
import com.cityproject.model.buildings.HealthBuilding;

/**
 * Applies only the fire protection and health-building effects each tick.
 * This separates those domain effects from residence happiness/health
 * calculations and keeps PopulationHappinessHealthManager focused.
 */
public class HealthAndFireProtectionManager {

    private final CityState city;

    public HealthAndFireProtectionManager(CityState city) {
        this.city = city;
    }

    public void applyHealthAndFireProtectionEffects() {
        resetFireProtection();
        for (Infrastructure building : city.getBuildings()) {

            if (building instanceof FireStation) {
                HasFireCoverage fs = (HasFireCoverage) building;
                AreaEffectHelper.applyInRadius(city, building.getX(), building.getY(), fs.getFireRadius(),
                        (cell, distance) -> cell.setFireProtection(true));
            }
            if (building instanceof HealthBuilding) {
                building.applyEffects(city);
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
