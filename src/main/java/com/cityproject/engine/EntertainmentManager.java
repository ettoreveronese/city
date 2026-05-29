package com.cityproject.engine;

import com.cityproject.engine.Helpers.AreaEffectHelper;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.EntertainmentComponent;

/**
 * Manages entertainment area effects.
 * Distributes happiness boost to cells within radius of active entertainment buildings.
 */
public class EntertainmentManager {

    private final CityState city;

    public EntertainmentManager(CityState city) {
        this.city = city;
    }

    public void applyEntertainment() {
        // Reset entertainment bonus
        for (int i = 0; i < city.getRows(); i++) {
            for (int j = 0; j < city.getCols(); j++) {
                city.getCell(i, j).setEntertainmentBonus(0);
            }
        }

        // Apply new bonus
        for (Infrastructure b : city.getBuildings()) {
            if (!b.isActive()) continue;
            
            if (b.hasComponent(EntertainmentComponent.class)) {
                EntertainmentComponent ec = b.getComponent(EntertainmentComponent.class);
                AreaEffectHelper.applyInRadius(city, b.getX(), b.getY(), ec.getRadius(),
                    (cell, distance) -> {
                        // The bonus could attenuate with distance or be flat. Let's make it flat for simplicity.
                        cell.setEntertainmentBonus(cell.getEntertainmentBonus() + ec.getHappinessBoost());
                    });
            }
        }
    }
}
