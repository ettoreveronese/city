package com.cityproject.presentation;

import java.net.URL;
import java.util.ResourceBundle;

import com.cityproject.engine.SimulationEngine;
import com.cityproject.model.Cell;
import com.cityproject.model.CityObserver;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.factory.BuildingFactory;
import com.cityproject.model.policy.GreenPolicy;
import com.cityproject.model.policy.IndustrialPolicy;
import com.cityproject.model.type.BuildingType;
import com.cityproject.model.components.Component;
import com.cityproject.model.components.EnergyComponent;
import com.cityproject.model.components.EntertainmentComponent;
import com.cityproject.model.components.FireCoverageComponent;
import com.cityproject.model.components.HealthComponent;
import com.cityproject.model.components.HousingComponent;
import com.cityproject.model.components.IncomeComponent;
import com.cityproject.model.components.MaintenanceComponent;
import com.cityproject.model.components.PollutionComponent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * OBSERVER PATTERN: implements CityObserver to receive updates after each tick.
 * MVC: this is the Controller — it owns no game logic, only UI logic.
 */
public class MainController implements Initializable, CityObserver {

    // --- FXML injected fields ---
    @FXML private GridPane cityGrid;
    @FXML private Label tickLabel, budgetLabel, populationLabel;
    @FXML private Label happinessLabel, healthLabel, policyLabel;
    @FXML private Label selectedLabel, energyLabel;
    @FXML private TextArea eventLog;
    @FXML private RadioButton visionNormal, visionPollution, visionFire, visionEntertainment;
    @FXML private VBox controlsVBox;

    // --- Simulation state ---
    private CityState cityState;
    private SimulationEngine engine;

    // --- UI state ---
    private String selectedBuildingType = null;
    private static final int CELL_SIZE = 28;
    private static final int GRID_ROWS = 20;
    private static final int GRID_COLS = 20;

    // --- Vision modes ---
    public enum VisionMode { NORMAL, POLLUTION, FIRE, ENTERTAINMENT }
    private VisionMode currentVision = VisionMode.NORMAL;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Create city state
        cityState = new CityState(GRID_ROWS, GRID_COLS, 50000);

        // 2. Place root road at center (bibbia: Strada-Radice always at center)
        int cx = GRID_ROWS / 2;
        int cy = GRID_COLS / 2;
        Infrastructure rootRoad = BuildingFactory.getInstance().createBuilding("ROOT_ROAD", cx, cy);
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

        // 7. Setup building tooltips
        setupBuildingTooltips();
    }

    private void setupBuildingTooltips() {
        for (javafx.scene.Node node : controlsVBox.getChildren()) {
            if (node instanceof Button btn) {
                String id = btn.getId();
                if (id == null || id.equals("DELETE") || id.equals("tickButton")) continue;

                BuildingType type = BuildingFactory.getInstance().getBuildingType(id);
                if (type != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(type.getName()).append("\n");
                    sb.append("Build Cost: $").append(type.getBuildCost()).append("\n");
                    
                    java.util.Map<Class<? extends Component>, Component> comps = type.getBaseComponents();

                    if (comps.containsKey(HousingComponent.class)) {
                        sb.append("Housing: ").append(((HousingComponent)comps.get(HousingComponent.class)).getCapacity()).append("\n");
                    }
                    if (comps.containsKey(IncomeComponent.class)) {
                        sb.append("Income: $").append(((IncomeComponent)comps.get(IncomeComponent.class)).getBaseIncome()).append("/tick\n");
                    }
                    if (comps.containsKey(MaintenanceComponent.class)) {
                        sb.append("Maintenance: $").append(((MaintenanceComponent)comps.get(MaintenanceComponent.class)).getMaintenanceCost()).append("/tick\n");
                    }
                    if (comps.containsKey(EnergyComponent.class)) {
                        EnergyComponent ec = (EnergyComponent)comps.get(EnergyComponent.class);
                        if (ec.getProduction() > 0) sb.append("Energy Prod: ").append(ec.getProduction()).append("\n");
                        if (ec.getConsumption() > 0) sb.append("Energy Cons: ").append(ec.getConsumption()).append("\n");
                    }
                    if (comps.containsKey(PollutionComponent.class)) {
                        PollutionComponent pc = (PollutionComponent)comps.get(PollutionComponent.class);
                        sb.append("Pollution: ").append(pc.getPollutionGenerated()).append(" (R: ").append(pc.getRadius()).append(")\n");
                    }
                    if (comps.containsKey(HealthComponent.class)) {
                        sb.append("Health: ").append(((HealthComponent)comps.get(HealthComponent.class)).getCapacity()).append("\n");
                    }
                    if (comps.containsKey(FireCoverageComponent.class)) {
                        sb.append("Fire Coverage: R: ").append(((FireCoverageComponent)comps.get(FireCoverageComponent.class)).getRadius()).append("\n");
                    }
                    if (comps.containsKey(EntertainmentComponent.class)) {
                        EntertainmentComponent ec = (EntertainmentComponent)comps.get(EntertainmentComponent.class);
                        sb.append("Entertainment: +").append(ec.getHappinessBoost()).append(" (R: ").append(ec.getRadius()).append(")\n");
                    }
                    
                    Tooltip tip = new Tooltip(sb.toString().trim());
                    tip.setShowDelay(Duration.millis(200));
                    Tooltip.install(btn, tip);
                }
            }
        }
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
        StackPane pane = new StackPane();
        pane.setPrefSize(CELL_SIZE, CELL_SIZE);
        pane.setStyle(getCellStyle(row, col));

        // pollution value label (centered)
        Label pollutionLabel = new Label();
        pollutionLabel.setId("pollutionLabel");
        pollutionLabel.setStyle("-fx-text-fill: white; -fx-font-size:10; -fx-font-weight:bold;");
        pollutionLabel.setMouseTransparent(true);
        pane.getChildren().add(pollutionLabel);

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

        // Vision mode: entertainment
        if (currentVision == VisionMode.ENTERTAINMENT) {
            int e = cell.getEntertainmentBonus();
            if (e == 0)   return "#13131f";
            if (e < 20)   return "#581c87"; // dark purple
            if (e < 50)   return "#7e22ce"; // medium purple
            if (e < 100)  return "#a855f7"; // bright purple
            return "#d8b4fe"; // light purple
        }

        // Normal mode: color by building type
        if (cell.isEmpty()) return "#13131f";

        Infrastructure s = cell.getStructure();
        if (!s.isActive()) return "#374151"; // inactive = gray

        return switch (s.getType().getId()) {
            case "ROAD", "ROOT_ROAD" -> "#6b7280";
            case "COTTAGE", "CONDO", "SKYSCRAPER" -> "#166534";
            case "FOOD", "METALLURGICAL", "PETROCHEMICAL" -> "#92400e";
            case "WIND", "SOLAR", "NUCLEAR" -> "#1e40af";
            case "COAL", "INCINERATOR", "OIL" -> "#7f1d1d";
            case "PARK", "NATURAL_RESERVE", "NATIONAL_PARK" -> "#14532d";
            case "CLINIC", "HOSPITAL" -> "#1e3a8a";
            case "FIRE_STATION" -> "#991b1b";
            case "CINEMA", "AMUSEMENT_PARK" -> "#9333ea";
            default -> "#4b5563";
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
            if (s != null && s.getType() != null && "ROOT_ROAD".equals(s.getType().getId())) {
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
        Infrastructure preview = BuildingFactory.getInstance().createBuilding(selectedBuildingType, row, col);
        if (cityState.getBudget() < preview.getType().getBuildCost()) {
            log("Not enough budget to build " + selectedBuildingType + "!");
            return;
        }

        // Place building
        cityState.setBudget(cityState.getBudget() - preview.getType().getBuildCost());
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
        else if (visionEntertainment.isSelected()) currentVision = VisionMode.ENTERTAINMENT;
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
        energyLabel.setText(cityState.getTotalEnergyProduced() + "/" + cityState.getTotalEnergyConsumed());
    }

    private void refreshGrid() {
        for (javafx.scene.Node node : cityGrid.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer col = GridPane.getColumnIndex(node);
            if (row == null || col == null) continue;
            if (node instanceof Pane pane) {
                pane.setStyle(getCellStyle(row, col));
                // update pollution label if present
                Cell cell = cityState.getCell(row, col);
                for (javafx.scene.Node child : pane.getChildren()) {
                    if (child instanceof Label lbl && "pollutionLabel".equals(lbl.getId())) {
                        if (currentVision == VisionMode.POLLUTION) {
                            lbl.setText(String.valueOf(cell.getPollution()));
                            lbl.setVisible(true);
                        } else {
                            lbl.setText("");
                            lbl.setVisible(false);
                        }
                    }
                }
            }
        }
    }

    private void log(String message) {
        eventLog.appendText("[T" + cityState.getTick() + "] " + message + "\n");
    }
}