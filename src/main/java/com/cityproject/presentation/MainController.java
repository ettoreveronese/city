package com.cityproject.presentation;

import com.cityproject.model.policy.IndustrialPolicy;
import com.cityproject.model.policy.CityPolicy;
import com.cityproject.model.policy.GreenPolicy;
import java.net.URL;
import java.util.ResourceBundle;

import com.cityproject.engine.SimulationEngine;
import com.cityproject.model.CityObserver;
import com.cityproject.model.CityState;
import com.cityproject.engine.config.ConfigManager;
import com.cityproject.engine.config.GameConfig;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.factory.BuildingFactory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MainController implements Initializable, CityObserver {

    @FXML private StatsController statsViewController;
    @FXML private GridController gridViewController;
    @FXML private ControlsController controlsViewController;
    @FXML private LogController logViewController;

    private CityState cityState;
    private SimulationEngine engine;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 0. Load Configuration
        GameConfig config = ConfigManager.getConfig();
        int rows = config.getGridRows();
        int cols = config.getGridCols();
        int budget = config.getStartingBudget();

        // 1. Create city state
        cityState = new CityState(rows, cols, budget);

        // 2. Place root road at center
        int cx = rows / 2;
        int cy = cols / 2;
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

    // --- Loading existing city ---
    public void loadExistingCity(CityState loadedCity, String policyName) {
        // 1. Unregister from old city
        if (this.cityState != null) {
            this.cityState.removeObserver(this);
        }

        // 2. Set new city
        this.cityState = loadedCity;

        // 3. Create engine with loaded policy
        // We'll map the policyName to the actual class
        CityPolicy loadedPolicy = new GreenPolicy(); // Default
        if ("IndustrialPolicy".equals(policyName)) loadedPolicy = new IndustrialPolicy();

        this.engine = new SimulationEngine(cityState, loadedPolicy);

        // 4. Register observer
        this.cityState.addObserver(this);

        // 5. Re-initialize sub-controllers
        if (controlsViewController != null) {
            controlsViewController.init(engine, this, cityState);
        }
        if (gridViewController != null) {
            gridViewController.init(cityState, controlsViewController, this);
        }

        // 6. Update UI
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

    public void logErrorEvent(String message) {
        if (logViewController != null) {
            int tick = cityState != null ? cityState.getTick() : 0;
            logViewController.appendErrorLog(message, tick);
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
        if (controlsViewController != null) {
            controlsViewController.updateLocks(city);
        }
    }
}