package com.cityproject.engine;

import com.cityproject.engine.Helpers.AreaEffectHelper;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.PollutionComponent;
import com.cityproject.model.policy.CityPolicy;

/**
 * Manages pollution spreading from buildings to cells.
 */
public class PollutionManager {

    private final CityState city;
    private final CityPolicy activePolicy;

    private static final double ATTENUATION = 0.5;

    public PollutionManager(CityState city, CityPolicy activePolicy) {
        this.city = city;
        this.activePolicy = activePolicy;
    }

    public void applyPollution() {
        // Reset pollution
        for (int i = 0; i < city.getRows(); i++) {
            for (int j = 0; j < city.getCols(); j++) {
                city.getCell(i, j).setPollution(0);
            }
        }

        double modifier = activePolicy.calculatePollutionModifier();

        for (Infrastructure b : city.getPollutionBuildings()) {
            if (!b.isActive()) continue;

            PollutionComponent polluter = b.getComponent(PollutionComponent.class);

            AreaEffectHelper.applyInRadius(city, b.getX(), b.getY(), polluter.getRadius(), 
                (cell, distance) -> {
                    int spread = (int)(polluter.getPollutionGenerated() * modifier / (distance*ATTENUATION + 1));
                    System.out.println(spread+ " " +polluter.getPollutionGenerated()+ " "+ distance+ " "+ modifier);
                    cell.setPollution(cell.getPollution() + spread);

                });
        }
    }
}
