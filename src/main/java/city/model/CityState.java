package city.model;

public class CityState {
    private int population = 0;
    private double budget = 10000.0;
    private double energy = 0.0;
    private double pollution = 0.0;
    private double happiness = 50.0;
    private double health = 100.0;

    // TODO: getters/setters

    public void updateMetrics() {
        // logic to prevent invalid parameters
        if (this.happiness > 100) this.happiness = 100;
        if (this.happiness < 0) this.happiness = 0;
        if (this.health > 100) this.health = 100;
        if (this.health < 0) this.health = 0;
    }

    public void resetTickValues() {
    }
}
