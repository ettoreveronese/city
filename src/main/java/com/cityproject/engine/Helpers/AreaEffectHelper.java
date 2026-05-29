package com.cityproject.engine.Helpers;

import com.cityproject.model.Cell;
import com.cityproject.model.CityState;

/**
 * Helper class for applying area effects on the grid.
 * Avoids duplicating the radius-loop logic across multiple building classes.
 * Used by PollutionManager, PopulationHappinessHealthManager, and building applyEffects().
 *
 * GRASP Pure Fabrication: this class doesn't represent a domain concept,
 * it exists purely to reduce code duplication and increase cohesion.
 */
public class AreaEffectHelper {

    /**
     * Functional interface for the effect to apply to each cell in range.
     * Receives the cell and its Manhattan distance from the source.
     */
    @FunctionalInterface
    public interface CellEffect {
        void apply(Cell cell, int distance);
    }

    /**
     * Applies an effect to all valid cells within a given radius
     * from position (originX, originY), using Manhattan distance.
     *
     * @param city      the city state (used to validate and retrieve cells)
     * @param originX   row of the source building
     * @param originY   column of the source building
     * @param radius    how many cells away the effect reaches
     * @param effect    the effect to apply to each cell
     */
    public static void applyInRadius(CityState city, int originX, int originY,
                                     int radius, CellEffect effect) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int nx = originX + dx;
                int ny = originY + dy;
                if (!city.isValid(nx, ny)) continue;
                int distance = Math.max(Math.abs(dx) , Math.abs(dy));
                effect.apply(city.getCell(nx, ny), distance);
            }
        }
    }
}
