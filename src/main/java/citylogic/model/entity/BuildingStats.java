package citylogic.model.entity;

/**
 * Contiene le statistiche statiche di un tipo di edificio (blueprint).
 */
public class BuildingStats {

    private int size;
    private int areaOfEffect;
    private int maintenanceCost;
    private double happinessImpact;
    private int populationCapacity;
    private double pollutionImpact;
    private double energyConsumption;

    public BuildingStats(int size, int areaOfEffect, int maintenanceCost,
                         double happinessImpact, int populationCapacity,
                         double pollutionImpact, double energyConsumption) {
        this.size = size;
        this.areaOfEffect = areaOfEffect;
        this.maintenanceCost = maintenanceCost;
        this.happinessImpact = happinessImpact;
        this.populationCapacity = populationCapacity;
        this.pollutionImpact = pollutionImpact;
        this.energyConsumption = energyConsumption;
    }

    public int getSize()               { return size; }
    public int getAreaOfEffect()       { return areaOfEffect; }
    public int getMaintenanceCost()    { return maintenanceCost; }
    public double getHappinessImpact() { return happinessImpact; }
    public int getPopulationCapacity() { return populationCapacity; }
    public double getPollutionImpact() { return pollutionImpact; }
    public double getEnergyConsumption() { return energyConsumption; }
}
