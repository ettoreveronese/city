package com.cityproject.presentation;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class LogController {

    @FXML private TextArea eventLog;

    public void appendLog(String message, int tick) {
        eventLog.appendText("[T" + tick + "] " + message + "\n");
    }
}
