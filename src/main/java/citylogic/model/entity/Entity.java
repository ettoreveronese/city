package citylogic.model.entity;

/**
 * Classe astratta base per tutte le entità posizionabili sulla griglia.
 */
public abstract class Entity {

    protected final int x;
    protected final int y;
    protected final BuildingType type;
    protected boolean active;
    protected boolean damaged; // danneggiato per questo tick: manutenzione x2, disattivato

    protected Entity(int x, int y, BuildingType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.active = true;
        this.damaged = false;
    }

    /** Marca l'edificio come danneggiato e lo disattiva per questo tick. */
    public void damage() {
        this.damaged = true;
        this.active = false;
    }

    /** Chiamato a fine tick per resettare lo stato di danno temporaneo. */
    public void resetDamage() {
        this.damaged = false;
        this.active = true;
    }

    public int getX()                     { return x; }
    public int getY()                     { return y; }
    public BuildingType getType()         { return type; }
    public boolean isActive()             { return active; }
    public boolean isDamaged()            { return damaged; }
    public void setActive(boolean active) { this.active = active; }
}
