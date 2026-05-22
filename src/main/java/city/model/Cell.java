package city.model;

import city.entity.Entity;

// represents a single cell inside the urban grid
// contains the coordinates and the entity positioned inside it
public class Cell {
    private final int x;
    private final int y;
    private Entity entity; // null if cell is empty

    // constructor for an empty cell
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.entity = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // returns the entity inside the cell 
    public Entity getEntity() {
        return entity;
    }

    // pass an entity object to position a new building or null to empty it
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    // check if cell is empty
    public boolean isEmpty() {
        return this.entity == null;
    }
}
