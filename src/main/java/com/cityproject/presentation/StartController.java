package com.cityproject.presentation;

import com.cityproject.engine.persistence.SaveManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import javafx.application.Platform;

public class StartController {

    @FXML
    private Label errorLabel;

    @FXML
    private void handleNewGame(ActionEvent event) {
        startGame(event, null);
    }

    @FXML
    private void handleLoadGame(ActionEvent event) {
        File saveFile = new File("savegame.json");
        if (!saveFile.exists()) {
            errorLabel.setText("Nessun salvataggio trovato!");
            errorLabel.setVisible(true);
            return;
        }

        SaveManager.LoadedGame loadedGame = SaveManager.loadGame("savegame.json");
        if (loadedGame == null) {
            errorLabel.setText("Errore durante il caricamento del file.");
            errorLabel.setVisible(true);
            return;
        }

        startGame(event, loadedGame);
    }

    @FXML
    private void handleExitGame(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    private void startGame(ActionEvent event, SaveManager.LoadedGame loadedGame) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cityproject/presentation/MainView.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            if (loadedGame != null) {
                mainController.loadExistingCity(loadedGame.city, loadedGame.policyName);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Errore critico: Impossibile avviare il gioco.");
            errorLabel.setVisible(true);
        }
    }
}
