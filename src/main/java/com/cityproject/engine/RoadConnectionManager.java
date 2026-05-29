package com.cityproject.engine;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;

/**
 * Handles Road Connection Verification (VCS algorithm).
 * Buildings are active only if connected to the root road via roads.
 */
public class RoadConnectionManager {

    private final CityState city;

    public RoadConnectionManager(CityState city) {
        this.city = city;
    }

    public void checkConnections() {
        Infrastructure rootRoad = findRootRoad();
        
        if (rootRoad == null) {
            for (Infrastructure b : city.getBuildings()) {
                if (!isRoad(b)) b.setActive(false);
            }
            return;
        }

        Set<String> connectedRoadIds = bfsConnectedRoads(rootRoad);

        for (Infrastructure b : city.getBuildings()) {
            if (isRoad(b)) continue;
            boolean connected = isAdjacentToConnectedRoad(b, connectedRoadIds);
            b.setActive(connected);
        }
    }

    private boolean isRoad(Infrastructure b) {
        return b != null && b.getType() != null && 
               ("ROAD".equals(b.getType().getId()) || "ROOT_ROAD".equals(b.getType().getId()));
    }

    private boolean isRootRoad(Infrastructure b) {
        return b != null && b.getType() != null && "ROOT_ROAD".equals(b.getType().getId());
    }

    private Infrastructure findRootRoad() {
        for (Infrastructure b : city.getBuildings())
            if (isRootRoad(b)) return b;
        return null;
    }

    private Set<String> bfsConnectedRoads(Infrastructure root) {
        Set<String> visited = new HashSet<>();
        Queue<Infrastructure> queue = new LinkedList<>();
        queue.add(root);
        visited.add(root.getId());

        while (!queue.isEmpty()) {
            Infrastructure current = queue.poll();
            int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
            for (int[] d : dirs) {
                int nx = current.getX() + d[0];
                int ny = current.getY() + d[1];

                if (!city.isValid(nx, ny)) continue;

                Infrastructure neighbor = city.getCell(nx, ny).getStructure();
                if (isRoad(neighbor) && !visited.contains(neighbor.getId())) {
                    visited.add(neighbor.getId());
                    queue.add(neighbor);
                }
            }
        }
        return visited;
    }

    private boolean isAdjacentToConnectedRoad(Infrastructure b, Set<String> connectedRoads) {
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};
        for (int i = 0; i < 4; i++) {
            int nx = b.getX() + dx[i];
            int ny = b.getY() + dy[i];
            if (!city.isValid(nx, ny)) continue;
            Infrastructure s = city.getCell(nx, ny).getStructure();
            if (isRoad(s) && connectedRoads.contains(s.getId())) return true;
        }
        return false;
    }
}
