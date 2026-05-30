package com.cityproject.engine.config;

/**
 * Classe per il parsing del file config.json che contiene i parametri principali del gioco.
 */
public class GameConfig {
    private int gridRows;
    private int gridCols;
    private int startingBudget;

    public int getGridRows() {
        return gridRows;
    }

    public void setGridRows(int gridRows) {
        this.gridRows = gridRows;
    }

    public int getGridCols() {
        return gridCols;
    }

    public void setGridCols(int gridCols) {
        this.gridCols = gridCols;
    }

    public int getStartingBudget() {
        return startingBudget;
    }

    public void setStartingBudget(int startingBudget) {
        this.startingBudget = startingBudget;
    }
}
