package city.entity;

import city.model.StateDelta;

// abstract class for every entity positionable on the grid
public abstract class Entity {
    private final int x;
    private final int y;
    private boolean active;
    private final BuildingBlueprint blueprint;

    // 
    protected Entity(int x, int y, BuildingBlueprint blueprint) {
        this.x = x;
        this.y = y;
        this.blueprint = blueprint;
        this.active = true; // newly postioned entites default to active
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BuildingBlueprint getBlueprint() {
        return blueprint;
    }

}
