package com.cityproject.presentation;

import java.net.URL;
import java.util.ResourceBundle;

import com.cityproject.business.SimulationEngine;
import com.cityproject.model.Cell;
import com.cityproject.model.CityObserver;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.buildings.Road;
import com.cityproject.model.factory.BuildingFactory;
import com.cityproject.model.policy.GreenPolicy;
import com.cityproject.model.policy.IndustrialPolicy;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * OBSERVER PATTERN: implements CityObserver to receive updates after each tick.
 * MVC: this is the Controller — it owns no game logic, only UI logic.
 */
public class MainController implements Initializable, CityObserver {

    // --- FXML injected fields ---
    @FXML private GridPane cityGrid;
    @FXML private Label tickLabel, budgetLabel, populationLabel;
    @FXML private Label happinessLabel, healthLabel, policyLabel;
    @FXML private Label selectedLabel;
    @FXML private TextArea eventLog;
    @FXML private RadioButton visionNormal, visionPollution, visionFire;

    // --- Simulation state ---
    private CityState cityState;
    private SimulationEngine engine;

    // --- UI state ---
    private String selectedBuildingType = null;
    private static final int CELL_SIZE = 28;
    private static final int GRID_ROWS = 20;
    private static final int GRID_COLS = 20;

    // --- Vision modes ---
    public enum VisionMode { NORMAL, POLLUTION, FIRE }
    private VisionMode currentVision = VisionMode.NORMAL;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Create city state
        cityState = new CityState(GRID_ROWS, GRID_COLS, 50000);

        // 2. Place root road at center (bibbia: Strada-Radice always at center)
        int cx = GRID_ROWS / 2;
        int cy = GRID_COLS / 2;
        Infrastructure rootRoad = BuildingFactory.create("ROOT_ROAD", cx, cy);
        cityState.addBuilding(rootRoad);
        cityState.getCell(cx, cy).setStructure(rootRoad);

        // 3. Create engine with default green policy
        engine = new SimulationEngine(cityState, new GreenPolicy());

        // 4. Register this controller as observer
        cityState.addObserver(this);

        // 5. Build the grid UI
        buildGrid();

        // 6. Initial UI update
        updateStats();
    }

    // -------------------------------------------------------------------------
    // Grid building
    // -------------------------------------------------------------------------
    private void buildGrid() {
        cityGrid.getChildren().clear();
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                Pane cell = createCellPane(row, col);
                cityGrid.add(cell, col, row);
            }
        }
    }

    private Pane createCellPane(int row, int col) {
        Pane pane = new Pane();
        pane.setPrefSize(CELL_SIZE, CELL_SIZE);
        pane.setStyle(getCellStyle(row, col));

        // Tooltip showing cell info
        Tooltip tip = new Tooltip();
        pane.setOnMouseEntered(e -> {
            Cell c = cityState.getCell(row, col);
            tip.setText(getCellTooltip(c));
            Tooltip.install(pane, tip);
        });

        // Click to place building
        pane.setOnMouseClicked(e -> handleCellClick(row, col));

        return pane;
    }

    private String getCellStyle(int row, int col) {
        Cell cell = cityState.getCell(row, col);
        String color = getCellColor(cell);
        return "-fx-background-color:" + color + ";" +
               "-fx-border-color:#2d2d3d; -fx-border-width:0.5;";
    }

    private String getCellColor(Cell cell) {
        // Vision mode: pollution
        if (currentVision == VisionMode.POLLUTION) {
            int p = cell.getPollution();
            if (p == 0)   return "#13131f";
            if (p < 10)   return "#365314";
            if (p < 25)   return "#713f12";
            if (p < 50)   return "#7c2d12";
            return "#450a0a";
        }

        // Vision mode: fire coverage
        if (currentVision == VisionMode.FIRE) {
            return cell.hasFireProtection() ? "#166534" : "#13131f";
        }

        // Normal mode: color by building type
        if (cell.isEmpty()) return "#13131f";

        Infrastructure s = cell.getStructure();
        if (!s.isActive()) return "#374151"; // inactive = gray

        return switch (s.getClass().getSimpleName()) {
            case "Road"               -> "#6b7280";
            case "Residence"          -> "#166534";
            case "ProductionBuilding" -> "#92400e";
            case "CleanPowerPlant"    -> "#1e40af";
            case "DirtyPowerPlant"    -> "#7f1d1d";
            case "NaturalArea"        -> "#14532d";
            case "HealthBuilding"     -> "#1e3a8a";
            case "FireStation"        -> "#991b1b";
            default                   -> "#4b5563";
        };
    }

    private String getCellTooltip(Cell cell) {
        if (cell.isEmpty()) return String.format("(%d,%d) Empty | Pollution: %d",
                cell.getX(), cell.getY(), cell.getPollution());
        Infrastructure s = cell.getStructure();
        return String.format("(%d,%d) %s\nActive: %s | Pollution: %d",
                cell.getX(), cell.getY(), s.getId(),
                s.isActive(), cell.getPollution());
    }

    // -------------------------------------------------------------------------
    // Cell click — place building
    // -------------------------------------------------------------------------
    private void handleCellClick(int row, int col) {
        if (selectedBuildingType == null) return;

        Cell cell = cityState.getCell(row, col);

        // DELETE mode
        if (selectedBuildingType.equals("DELETE")) {
            if (cell.isEmpty()) {
                log("Cell (" + row + "," + col + ") is already empty.");
                return;
            }
            Infrastructure s = cell.getStructure();
            if (s instanceof Road r && r.isRoot()) {
                log("Cannot delete the root road!");
                return;
            }
            cityState.removeBuilding(s);
            cell.setStructure(null);
            log("Deleted " + s.getId() + " at (" + row + "," + col + ")");
            refreshGrid();
            updateStats();
            return;
        }


        // BUILD mode — codice esistente invariato
        if (!cell.isEmpty()) {
            log("Cell (" + row + "," + col + ") is already occupied.");
            return;
        }

        // Check budget
        Infrastructure preview = BuildingFactory.create(selectedBuildingType, row, col);
        if (cityState.getBudget() < preview.getBuildCost()) {
            log("Not enough budget to build " + selectedBuildingType + "!");
            return;
        }

        // Place building
        cityState.setBudget(cityState.getBudget() - preview.getBuildCost());
        cityState.addBuilding(preview);
        cell.setStructure(preview);


        log("Built " + selectedBuildingType + " at (" + row + "," + col + ")");
        refreshGrid();
        updateStats();
    }

   
    // -------------------------------------------------------------------------
    // FXML handlers
    // -------------------------------------------------------------------------
    @FXML
    private void handleTick() {
        engine.tick();
        // onCityUpdated() will be called automatically via Observer
    }

    @FXML
    private void handleSelectBuilding(javafx.event.ActionEvent e) {
        Button btn = (Button) e.getSource();
        selectedBuildingType = btn.getId();
        selectedLabel.setText(selectedBuildingType);
    }

    @FXML
    private void handleGreenPolicy() {
        engine.setPolicy(new GreenPolicy());
        policyLabel.setText("Green Policy");
        log("Policy changed to Green Policy.");
    }

    @FXML
    private void handleIndustrialPolicy() {
        engine.setPolicy(new IndustrialPolicy());
        policyLabel.setText("Industrial Policy");
        log("Policy changed to Industrial Policy.");
    }

    @FXML
    private void handleVisionChange() {
        if (visionPollution.isSelected()) currentVision = VisionMode.POLLUTION;
        else if (visionFire.isSelected()) currentVision = VisionMode.FIRE;
        else currentVision = VisionMode.NORMAL;
        refreshGrid();
    }

    // -------------------------------------------------------------------------
    // Observer Pattern — called after every tick
    // -------------------------------------------------------------------------
    @Override
    public void onCityUpdated(CityState city) {
        refreshGrid();
        updateStats();
    }

    // -------------------------------------------------------------------------
    // UI update helpers
    // -------------------------------------------------------------------------
    private void updateStats() {
        tickLabel.setText(String.valueOf(cityState.getTick()));
        budgetLabel.setText("$" + cityState.getBudget());
        populationLabel.setText(String.valueOf(cityState.getPopulation()));
        happinessLabel.setText(String.format("%.1f%%", cityState.getGlobalHappiness()));
        healthLabel.setText(String.format("%.1f%%", cityState.getGlobalHealth()));
        policyLabel.setText(engine.getActivePolicy().getName());
    }

    private void refreshGrid() {
        for (javafx.scene.Node node : cityGrid.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer col = GridPane.getColumnIndex(node);
            if (row == null || col == null) continue;
            if (node instanceof Pane pane) {
                pane.setStyle(getCellStyle(row, col));
            }
        }
    }

    private void log(String message) {
        eventLog.appendText("[T" + cityState.getTick() + "] " + message + "\n");
    }
}