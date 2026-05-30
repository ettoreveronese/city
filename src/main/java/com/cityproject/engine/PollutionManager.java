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
    private CityPolicy activePolicy;

    private static final double ATTENUATION = 0.5;

    private int[][] pollutionGrid;
    private int gridRows = 0;
    private int gridCols = 0;

    public PollutionManager(CityState city, CityPolicy activePolicy) {
        this.city = city;
        this.activePolicy = activePolicy;
    }

    public void setActivePolicy(CityPolicy activePolicy) {
        this.activePolicy = activePolicy;
    }

    public void applyPollution() {
        int rows = city.getRows();
        int cols = city.getCols();
        
        // Reallocate only if dimensions change (e.g. first tick or grid resize)
        if (pollutionGrid == null || rows != gridRows || cols != gridCols) {
            pollutionGrid = new int[rows][cols];
            gridRows = rows;
            gridCols = cols;
        } else {
            // Clear existing grid
            for (int i = 0; i < rows; i++) {
                java.util.Arrays.fill(pollutionGrid[i], 0);
            }
        }

        double modifier = activePolicy.calculatePollutionModifier();

        for (Infrastructure b : city.getPollutionBuildings()) {
            if (!b.isActive()) continue;

            PollutionComponent polluter = b.getComponent(PollutionComponent.class);

            AreaEffectHelper.applyInRadius(city, b.getX(), b.getY(), polluter.getRadius(), 
                (cell, distance) -> {
                    int spread = (int)(polluter.getPollutionGenerated() * modifier / (distance*ATTENUATION + 1));
                    pollutionGrid[cell.getX()][cell.getY()] += spread;
                });
        }
        
        // Apply accumulated pollution to cells, clamping at 0
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                city.getCell(i, j).setPollution(pollutionGrid[i][j]);
            }
        }
    }
}
