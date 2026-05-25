package com.cityproject.engine;

import com.cityproject.model.Cell;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasPollution;
import com.cityproject.model.policy.CityPolicy;

/**
 * Manages pollution spreading from buildings to cells.
 */
public class PollutionManager {

    private final CityState city;
    private final CityPolicy activePolicy;

    public PollutionManager(CityState city, CityPolicy activePolicy) {
        this.city = city;
        this.activePolicy = activePolicy;
    }

    public void applyPollution() {
        // Reset pollution first
        for (int i = 0; i < city.getRows(); i++)
            for (int j = 0; j < city.getCols(); j++)
                city.getCell(i, j).setPollution(0);

        // Apply pollution modifier from active policy
        double modifier = activePolicy.calculatePollutionModifier();

        for (HasPollution polluter : city.getPollutions()) {
            Infrastructure b = (Infrastructure) polluter;
            if (!b.isActive()) continue;
            
            int range = polluter.getRange();
            for (int dx = -range; dx <= range; dx++) {
                for (int dy = -range; dy <= range; dy++) {
                    int nx = b.getX() + dx;
                    int ny = b.getY() + dy;
                    if (!city.isValid(nx, ny)) continue;
                    Cell cell = city.getCell(nx, ny);
                    int distance = Math.abs(dx) + Math.abs(dy);
                    int spread = (int)(polluter.getPollutionLevel() * modifier / (distance + 1));
                    cell.setPollution(cell.getPollution() + spread);
                }
            }
        }
    }
}
