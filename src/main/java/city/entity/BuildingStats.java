package city.entity;

public class BuildingStats {
    private final int size;
    private final int areaOfEffect;
    private final int income;
    private final int maintenanceCost;
    private final int populationCapacity;
    private final double happinessImpact;
    private final double pollutionImpact;
    private final double healthImpact;
    private final double energyConsumption;

    public BuildingStats(int size, int areaOfEffect, int income, int maintenanceCost, 
                         int populationCapacity, double happinessImpact, double pollutionImpact, 
                         double healthImpact, double energyConsumption) {
        this.size = size;
        this.areaOfEffect = areaOfEffect;
        this.income = income;
        this.maintenanceCost = maintenanceCost;
        this.populationCapacity = populationCapacity;
        this.happinessImpact = happinessImpact;
        this.pollutionImpact = pollutionImpact;
        this.healthImpact = healthImpact;
        this.energyConsumption = energyConsumption;
    }

    // only getter methods, BuildingStats is an immutable object
    public int getSize() { return size; }
    public int getAreaOfEffect() { return areaOfEffect; }
    public int getIncome() { return income; }
    public int getMaintenanceCost() { return maintenanceCost; }
    public int getPopulationCapacity() { return populationCapacity; }
    public double getHappinessImpact() { return happinessImpact; }
    public double getPollutionImpact() { return pollutionImpact; }
    public double getHealthImpact() { return healthImpact; }
    public double getEnergyConsumption() { return energyConsumption; }
}
