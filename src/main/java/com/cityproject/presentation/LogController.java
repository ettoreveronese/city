package com.cityproject.presentation;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.application.Platform;

public class LogController {

    @FXML private VBox logContainer;

    public void appendLog(String message, int tick) {
        addLogLabel("[T" + tick + "] " + message, "#9ca3af");
    }

    public void appendErrorLog(String message, int tick) {
        addLogLabel("[T" + tick + "] " + message, "#ef4444"); // Red color
    }
    
    private void addLogLabel(String text, String color) {
        Platform.runLater(() -> {
            Label label = new Label(text);
            label.setStyle("-fx-text-fill: " + color + "; -fx-font-size:10;");
            label.setWrapText(true);
            logContainer.getChildren().add(label);
            
            // Limit to 50 logs to prevent memory leak
            if (logContainer.getChildren().size() > 50) {
                logContainer.getChildren().remove(0);
            }
        });
    }
}
