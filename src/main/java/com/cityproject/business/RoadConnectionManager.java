package com.cityproject.business;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.buildings.Road;

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
        Road rootRoad = findRootRoad();
        if (rootRoad == null) return;

        Set<String> connectedRoadIds = bfsConnectedRoads(rootRoad);

        // A building is connected if at least one adjacent cell has a connected road
        for (Infrastructure b : city.getBuildings()) {
            if (b instanceof Road) continue;
            boolean connected = isAdjacentToConnectedRoad(b, connectedRoadIds);
            b.setActive(connected);
        }
    }

    private Road findRootRoad() {
        for (Infrastructure b : city.getBuildings())
            if (b instanceof Road r && r.isRoot()) return r;
        return null;
    }

    private Set<String> bfsConnectedRoads(Road root) {
        Set<String> visited = new HashSet<>();
        Queue<Road> queue = new LinkedList<>();
        queue.add(root);
        visited.add(root.getId());

        while (!queue.isEmpty()) {
            Road current = queue.poll();
            // Check 4 adjacent cells for other roads
            int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
            for (int[] d : dirs) {
                int nx = current.getX() + d[0];
                int ny = current.getY() + d[1];
                if (!city.isValid(nx, ny)) continue;
                Infrastructure neighbor = city.getCell(nx, ny).getStructure();
                if (neighbor instanceof Road r && !visited.contains(r.getId())) {
                    visited.add(r.getId());
                    queue.add(r);
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
            if (s instanceof Road && connectedRoads.contains(s.getId())) return true;
        }
        return false;
    }
}
