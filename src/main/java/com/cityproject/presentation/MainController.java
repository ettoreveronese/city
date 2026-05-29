package com.cityproject.presentation;

import java.net.URL;
import java.util.ResourceBundle;

import com.cityproject.engine.SimulationEngine;
import com.cityproject.model.CityObserver;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.factory.BuildingFactory;
import com.cityproject.model.policy.GreenPolicy;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MainController implements Initializable, CityObserver {

    @FXML private StatsController statsViewController;
    @FXML private GridController gridViewController;
    @FXML private ControlsController controlsViewController;
    @FXML private LogController logViewController;

    private CityState cityState;
    private SimulationEngine engine;

    private static final int GRID_ROWS = 30;
    private static final int GRID_COLS = 30;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Create city state
        cityState = new CityState(GRID_ROWS, GRID_COLS, 50000);

        // 2. Place root road at center
        int cx = GRID_ROWS / 2;
        int cy = GRID_COLS / 2;
        Infrastructure rootRoad = BuildingFactory.getInstance().createBuilding("ROOT_ROAD", cx, cy);
        cityState.addBuilding(rootRoad);
        cityState.getCell(cx, cy).setStructure(rootRoad);

        // 3. Create engine with default green policy
        engine = new SimulationEngine(cityState, new GreenPolicy());

        // 4. Register this controller as observer
        cityState.addObserver(this);

        // 5. Initialize sub-controllers
        if (controlsViewController != null) {
            controlsViewController.init(engine, this, cityState);
        }
        if (gridViewController != null) {
            gridViewController.init(cityState, controlsViewController, this);
        }

        // 6. Initial UI update
        onCityUpdated(cityState);
    }

    // --- Mediator callbacks ---

    public void onBuildingSelected(String buildingType) {
        if (statsViewController != null) {
            statsViewController.updateSelectedBuilding(buildingType);
        }
    }

    public void onVisionChanged() {
        if (gridViewController != null) {
            gridViewController.refreshGrid();
        }
    }

    public void onPolicyChanged() {
        onCityUpdated(cityState);
    }

    public void logEvent(String message) {
        if (logViewController != null) {
            int tick = cityState != null ? cityState.getTick() : 0;
            logViewController.appendLog(message, tick);
        }
    }

    // --- Observer callback ---
    
    @Override
    public void onCityUpdated(CityState city) {
        if (statsViewController != null) {
            String policyName = engine != null ? engine.getActivePolicy().getName() : "-";
            statsViewController.updateStats(city, policyName);
        }
        if (gridViewController != null) {
            gridViewController.refreshGrid();
        }
    }
}