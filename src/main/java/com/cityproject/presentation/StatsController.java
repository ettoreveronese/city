package com.cityproject.presentation;

import com.cityproject.model.CityState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatsController {

    @FXML private Label tickLabel;
    @FXML private Label budgetLabel;
    @FXML private Label populationLabel;
    @FXML private Label happinessLabel;
    @FXML private Label healthLabel;
    @FXML private Label policyLabel;
    @FXML private Label energyLabel;
    @FXML private Label selectedLabel;

    public void updateStats(CityState cityState, String policyName) {
        tickLabel.setText(String.valueOf(cityState.getTick()));
        budgetLabel.setText("$" + cityState.getBudget());
        populationLabel.setText(String.valueOf(cityState.getPopulation()));
        happinessLabel.setText(String.format("%.1f%%", cityState.getGlobalHappiness()));
        healthLabel.setText(String.format("%.1f%%", cityState.getGlobalHealth()));
        policyLabel.setText(policyName);
        energyLabel.setText(cityState.getTotalEnergyProduced() + "/" + cityState.getTotalEnergyConsumed());
    }

    public void updateSelectedBuilding(String buildingType) {
        selectedLabel.setText(buildingType != null ? buildingType : "none");
    }
}
