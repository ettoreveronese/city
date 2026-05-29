package com.cityproject.engine;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.type.BuildingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoadConnectionManagerTest {

    private CityState city;
    private RoadConnectionManager rcm;
    private BuildingType rootType;
    private BuildingType roadType;
    private BuildingType buildingType;

    @BeforeEach
    public void setup() {
        city = new CityState(10, 10, 1000);
        rcm = new RoadConnectionManager(city);
        
        rootType = new BuildingType("ROOT_ROAD", "Root Road", 0);
        roadType = new BuildingType("ROAD", "Road", 0);
        buildingType = new BuildingType("COTTAGE", "Cottage", 0);
    }

    private Infrastructure addBuilding(BuildingType type, int x, int y) {
        Infrastructure b = new Infrastructure(java.util.UUID.randomUUID().toString(), type, x, y);
        city.addBuilding(b);
        city.getCell(x, y).setStructure(b);
        return b;
    }

    @Test
    public void testBuildingConnectedToRootRoad() {
        Infrastructure root = addBuilding(rootType, 5, 5);
        Infrastructure road = addBuilding(roadType, 5, 6);
        Infrastructure cottage = addBuilding(buildingType, 5, 7);

        rcm.checkConnections();

        assertTrue(cottage.isActive(), "Cottage should be active when connected to ROOT_ROAD via ROAD");
    }

    @Test
    public void testBuildingDisconnectedFromRootRoad() {
        Infrastructure root = addBuilding(rootType, 5, 5);
        // Missing road at 5,6
        Infrastructure cottage = addBuilding(buildingType, 5, 7);

        rcm.checkConnections();

        assertFalse(cottage.isActive(), "Cottage should be inactive when disconnected from ROOT_ROAD");
    }

    @Test
    public void testNoRootRoadDeactivatesAll() {
        // No ROOT_ROAD added
        Infrastructure road = addBuilding(roadType, 5, 6);
        Infrastructure cottage = addBuilding(buildingType, 5, 7);
        cottage.setActive(true); // manually set active to ensure it gets deactivated

        rcm.checkConnections();

        assertFalse(cottage.isActive(), "Cottage should be inactive when there is no ROOT_ROAD in the city");
    }
}
