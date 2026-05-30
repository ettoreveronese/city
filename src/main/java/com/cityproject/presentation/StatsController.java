package com.cityproject.presentation;

import com.cityproject.model.CityState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.cityproject.engine.persistence.SaveManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StatsController {

    @FXML private Label tickLabel;
    @FXML private Label budgetLabel;
    @FXML private Label populationLabel;
    @FXML private Label happinessLabel;
    @FXML private Label healthLabel;
    @FXML private Label policyLabel;
    @FXML private Label energyLabel;
    @FXML private Label selectedLabel;

    private CityState currentCity;
    private String currentPolicy;

    public void updateStats(CityState cityState, String policyName) {
        this.currentCity = cityState;
        this.currentPolicy = policyName;
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

    @FXML
    public void handleSaveGame(ActionEvent event) {
        if (currentCity != null && currentPolicy != null) {
            SaveManager.saveGame(currentCity, currentPolicy, "savegame.json");
            System.out.println("Partita Salvata da StatsView!");
        }
    }

    @FXML
    public void handleExitToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cityproject/presentation/StartView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
