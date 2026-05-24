package citylogic.model;

/**
 * Stato globale della città. Valori in [0,100] per happiness e health.
 */
public class CityState {

    private double budget;
    private int    population;
    private double energy;
    private double pollution;
    private double happiness; // [0, 100]
    private double health;    // [0, 100]

    public CityState(double budget, int population, double energy,
                     double pollution, double happiness, double health) {
        this.budget     = budget;
        this.population = population;
        this.energy     = energy;
        this.pollution  = pollution;
        this.happiness  = happiness;
        this.health     = health;
    }

    /** Applica un StateDelta e clampla i valori nei range validi. */
    public void applyDelta(StateDelta delta) {
        budget     += delta.getDeltaBudget();
        population += (int) delta.getDeltaPopulation();
        energy     += delta.getDeltaEnergy();
        pollution  += delta.getDeltaPollution();
        happiness   = clamp(happiness + delta.getDeltaHappiness(), 0, 100);
        health      = clamp(health    + delta.getDeltaHealth(),    0, 100);
    }

    public void resetToValues(double budget, int population, double energy,
                              double pollution, double happiness, double health) {
        this.budget     = budget;
        this.population = population;
        this.energy     = energy;
        this.pollution  = pollution;
        this.happiness  = happiness;
        this.health     = health;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    public double getBudget()     { return budget; }
    public int    getPopulation() { return population; }
    public double getEnergy()     { return energy; }
    public double getPollution()  { return pollution; }
    public double getHappiness()  { return happiness; }
    public double getHealth()     { return health; }

    public void setBudget(double budget)       { this.budget = budget; }
    public void setPopulation(int population)  { this.population = population; }
    public void setEnergy(double energy)       { this.energy = energy; }
    public void setPollution(double pollution) { this.pollution = pollution; }
    public void setHappiness(double happiness) { this.happiness = clamp(happiness, 0, 100); }
    public void setHealth(double health)       { this.health = clamp(health, 0, 100); }
}
