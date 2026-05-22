package it.unipd.citylogic.model;

// immutable object
public final class StateDelta {
    private final double deltaBudget;
    private final double deltaPopulation;
    private final double deltaPollution;
    private final double deltaHappiness;
    private final double deltaHealth;
    private final double deltaEnergy;

    public StateDelta(double deltaBudget, double deltaPopulation, double deltaPollution, 
                      double deltaHappiness, double deltaHealth, double deltaEnergy) {
        this.deltaBudget = deltaBudget;
        this.deltaPopulation = deltaPopulation;
        this.deltaPollution = deltaPollution;
        this.deltaHappiness = deltaHappiness;
        this.deltaHealth = deltaHealth;
        this.deltaEnergy = deltaEnergy;
    }

    public double getDeltaBudget() { return deltaBudget; }
    public double getDeltaPopulation() { return deltaPopulation; }
    public double getDeltaPollution() { return deltaPollution; }
    public double getDeltaHappiness() { return deltaHappiness; }
    public double getDeltaHealth() { return deltaHealth; }
    public double getDeltaEnergy() { return deltaEnergy; }
}
