package com.cityproject.presentation;

import com.cityproject.engine.SimulationEngine;
import com.cityproject.model.components.Component;
import com.cityproject.model.components.EnergyComponent;
import com.cityproject.model.components.EntertainmentComponent;
import com.cityproject.model.components.FireCoverageComponent;
import com.cityproject.model.components.HealthComponent;
import com.cityproject.model.components.HousingComponent;
import com.cityproject.model.components.IncomeComponent;
import com.cityproject.model.components.MaintenanceComponent;
import com.cityproject.model.components.PollutionComponent;
import com.cityproject.model.factory.BuildingFactory;
import com.cityproject.model.policy.GreenPolicy;
import com.cityproject.model.policy.IndustrialPolicy;
import com.cityproject.model.type.BuildingType;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/// Controller per la gestione dei controlli laterali (selezione edificio, cambio visione, applicazione policy)
public class ControlsController {

    public enum VisionMode { NORMAL, POLLUTION, FIRE, ENTERTAINMENT, LOCAL_HAPPINESS, LOCAL_HEALTH }

    @FXML private RadioButton visionNormal;
    @FXML private RadioButton visionPollution;
    @FXML private RadioButton visionFire;
    @FXML private RadioButton visionEntertainment;
    @FXML private RadioButton visionHappiness;
    @FXML private RadioButton visionHealth;
    @FXML private VBox controlsVBox;

    private VisionMode currentVision = VisionMode.NORMAL;
    private String selectedBuildingType = null;
    
    private SimulationEngine engine;
    private MainController mainController;
    private com.cityproject.model.CityState cityState;

    public void init(SimulationEngine engine, MainController mainController, com.cityproject.model.CityState cityState) {
        this.engine = engine;
        this.mainController = mainController;
        this.cityState = cityState;
        setupBuildingTooltips();
    }

    public VisionMode getCurrentVision() { return currentVision; }
    public String getSelectedBuildingType() { return selectedBuildingType; }

    @FXML
    private void handleTick() {
        if (engine != null) engine.tick();
    }

    @FXML
    private void handleSelectBuilding(javafx.event.ActionEvent e) {
        Button btn = (Button) e.getSource();
        String id = btn.getId();
        
        if (id != null && !id.equals("DELETE") && cityState != null) {
            BuildingType type = BuildingFactory.getInstance().getBuildingType(id);
            if (type != null && type.getUnlockPopulation() > cityState.getPopulation()) {
                logError("Devi raggiungere " + type.getUnlockPopulation() + " Abitanti per sbloccare " + type.getName() + "!");
                return;
            }
        }
        
        selectedBuildingType = id;
        if (mainController != null) {
            mainController.onBuildingSelected(selectedBuildingType);
        }
    }

    @FXML
    private void handleGreenPolicy() {
        if (engine != null) {
            engine.setPolicy(new GreenPolicy());
            log("Policy changed to Green Policy.");
            if (mainController != null) mainController.onPolicyChanged();
        }
    }

    @FXML
    private void handleIndustrialPolicy() {
        if (engine != null) {
            engine.setPolicy(new IndustrialPolicy());
            log("Policy changed to Industrial Policy.");
            if (mainController != null) mainController.onPolicyChanged();
        }
    }

    @FXML
    private void handleVisionChange() {
        if (visionPollution.isSelected()) currentVision = VisionMode.POLLUTION;
        else if (visionFire.isSelected()) currentVision = VisionMode.FIRE;
        else if (visionEntertainment.isSelected()) currentVision = VisionMode.ENTERTAINMENT;
        else if (visionHappiness.isSelected()) currentVision = VisionMode.LOCAL_HAPPINESS;
        else if (visionHealth.isSelected()) currentVision = VisionMode.LOCAL_HEALTH;
        else currentVision = VisionMode.NORMAL;

        if (mainController != null) mainController.onVisionChanged();
    }

    public void log(String message) {
        if (mainController != null) {
            mainController.logEvent(message);
        }
    }

    public void logError(String message) {
        if (mainController != null) {
            mainController.logErrorEvent(message);
        }
    }

    // Imposta i tooltip per ogni pulsante edificio, mostrando le statistiche e se è bloccato o meno
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
                        sb.append("Pollution: ").append(pc.getPollutionGenerated()).append(" (R: ").append(pc.getRadius()).append("\n");
                    }
                    if (comps.containsKey(HealthComponent.class)) {
                        sb.append("Health: ").append(((HealthComponent)comps.get(HealthComponent.class)).getCapacity()).append("\n");
                    }
                    if (comps.containsKey(FireCoverageComponent.class)) {
                        FireCoverageComponent fc = (FireCoverageComponent)comps.get(FireCoverageComponent.class);
                        sb.append("Fire Coverage: R: ").append(fc.getRadius()).append("\n");
                    }
                    if (comps.containsKey(EntertainmentComponent.class)) {
                        EntertainmentComponent ec = (EntertainmentComponent)comps.get(EntertainmentComponent.class);
                        sb.append("Entertainment: +").append(ec.getHappinessBoost()).append(" (R: ").append(ec.getRadius()).append(")\n");
                    }
                    // Controlla se l'edificio è bloccato o meno in base alla popolazione attuale
                    int unlockPop = type.getUnlockPopulation();
                    if (unlockPop > cityState.getPopulation()) {
                        sb.insert(0, "[BLOCCATO] Richiede " + unlockPop + " Abitanti\n\n");
                        btn.setOpacity(0.4);
                    } else {
                        btn.setOpacity(1.0);
                    }
                    
                    Tooltip tip = new Tooltip(sb.toString().trim());
                    tip.setShowDelay(Duration.millis(200));
                    btn.setTooltip(tip);
                }
            }
        }
    }

    public void updateLocks(com.cityproject.model.CityState cityState) {
        this.cityState = cityState;
        setupBuildingTooltips();
    }
}
