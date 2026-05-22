package city.entity;

// immutable configuration of a building
public class BuildingBlueprint {
    
    private final BuildingType type;
    private final int requiredPopulation; // required population to build the building
    private final int requiredBudget; // required budget 
    private final BuildingStats stats; // stats of the building, describe its effect on the city

    public BuildingBlueprint(BuildingType type, int requiredPopulation, int requiredBudget, BuildingStats stats) {
        this.type = type;
        this.requiredPopulation = requiredPopulation;
        this.requiredBudget = requiredBudget;
        this.stats = stats;
    }

    public BuildingType getType() {
        return type;
    }

    public int getRequiredPopulation() {
        return requiredPopulation;
    }

    public int getRequiredBudget() {
        return requiredBudget;
    }

    public BuildingStats getStats() {
        return stats;
    }
}
