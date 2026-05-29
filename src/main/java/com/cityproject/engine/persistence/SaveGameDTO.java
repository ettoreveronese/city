package com.cityproject.engine.persistence;

import java.util.List;

public class SaveGameDTO {
    public int rows;
    public int cols;
    public int budget;
    public int population;
    public int tick;
    public double globalHappiness;
    public double globalHealth;
    public String activePolicyName;
    public List<SavedBuildingDTO> buildings;

    public SaveGameDTO() {} // For Gson
}
