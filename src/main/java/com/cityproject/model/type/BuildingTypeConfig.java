package com.cityproject.model.type;

/**
 * POJO per il parsing Gson del file buildings.json.
 */
public class BuildingTypeConfig {
    public String id;
    public String name;
    public String emoji;
    public int buildCost;
    public Integer unlockPopulation;

    // Component configurations (optional in json)
    public Integer housingCapacity;
    public Integer baseIncome;
    public Integer energyConsumption;
    public Integer energyProduction;
    public Integer maintenanceCost;
    public Integer pollutionGenerated;
    public Integer pollutionRadius;
    public Integer fireCoverageRadius;
    public Integer healthCapacity;
    public Integer happinessBoost;
    public Integer entertainmentRadius;
}
