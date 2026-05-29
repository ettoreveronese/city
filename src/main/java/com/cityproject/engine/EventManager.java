package com.cityproject.engine;

import java.util.ArrayList;
import java.util.List;

import com.cityproject.model.Cell;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;

/**
 * Manages random events like fires during simulation.
 */
public class EventManager {
    private final double FIRE_BASE_PROBABILITY = 0.003; // Base fire probability per building
    private final CityState city;

    public EventManager(CityState city) {
        this.city = city;
    }

    public void processEvents() {
        fireEvent();
        // TODO: add CovidEvent, TowerCollapseEvent in next iteration
    }

    private boolean isRoad(Infrastructure b) {
        return b != null && b.getType() != null && "Road".equals(b.getType().getId());
    }

    private void fireEvent() {
        // Fire probability increases with number of buildings
        long buildingCount = city.getBuildings().stream()
            .filter(b -> !isRoad(b)).count();
        double fireProbability = buildingCount * FIRE_BASE_PROBABILITY;

        if (Math.random() > fireProbability) return;

        // Pick a random non-road, non-root structure
        List<Infrastructure> targets = new ArrayList<>();
        for (Infrastructure b : city.getBuildings())
            if (!isRoad(b) && b.isActive()) targets.add(b);

        if (targets.isEmpty()) return;

        Infrastructure target = targets.get((int)(Math.random() * targets.size()));
        Cell targetCell = city.getCell(target.getX(), target.getY());

        if (targetCell.hasFireProtection()) {
            System.out.println("[EVENT] Fire at " + target.getId() + " — contained by fire station.");
            city.removeBuilding(target);
            targetCell.setStructure(null);
        } else {
            System.out.println("[EVENT] Fire at " + target.getId() + " — spreading!");
            destroyWithNeighbors(target);
        }
    }

    private void destroyWithNeighbors(Infrastructure origin) {
        int range = 1;
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                int nx = origin.getX() + dx;
                int ny = origin.getY() + dy;
                if (!city.isValid(nx, ny)) continue;
                Cell cell = city.getCell(nx, ny);
                Infrastructure s = cell.getStructure();
                if (s != null && !isRoad(s)) {
                    city.removeBuilding(s);
                    cell.setStructure(null);
                    System.out.println("[EVENT] Building destroyed at " + s.getId());
                }
            }
        }
    }
}
