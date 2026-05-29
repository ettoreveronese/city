package com.cityproject.engine.config;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Manager che carica e fornisce l'accesso alla configurazione di base del gioco.
 */
public class ConfigManager {
    private static GameConfig currentConfig;

    public static GameConfig getConfig() {
        if (currentConfig == null) {
            loadConfig();
        }
        return currentConfig;
    }

    private static void loadConfig() {
        try {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(
                ConfigManager.class.getResourceAsStream("/config.json")
            );
            currentConfig = gson.fromJson(reader, GameConfig.class);
            reader.close();
            if (currentConfig == null) {
                currentConfig = createDefaultConfig();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load config.json, using defaults.");
            currentConfig = createDefaultConfig();
        }
    }

    private static GameConfig createDefaultConfig() {
        GameConfig c = new GameConfig();
        c.setGridRows(50);
        c.setGridCols(50);
        c.setStartingBudget(5000);
        return c;
    }
}
