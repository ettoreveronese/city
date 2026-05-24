package citylogic.model;

import citylogic.model.entity.Entity;

/**
 * Singola cella della griglia. Può contenere al massimo una Entity.
 */
public class Cell {

    private final int x;
    private final int y;
    private Entity entity;
    private boolean fireProtection; // true se coperta dall'area di una FireStation

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.entity = null;
        this.fireProtection = false;
    }

    public boolean isEmpty()         { return entity == null; }

    public int     getX()            { return x; }
    public int     getY()            { return y; }
    public Entity  getEntity()       { return entity; }
    public boolean isFireProtected() { return fireProtection; }

    public void setEntity(Entity entity)              { this.entity = entity; }
    public void setFireProtection(boolean protection) { this.fireProtection = protection; }
}
