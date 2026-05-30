package com.cityproject.model;

import com.cityproject.model.components.Component;
import com.cityproject.model.type.BuildingType;
import java.util.HashMap;
import java.util.Map;

/**
 * Entità concreta principale nel pattern ECS.
 * Contiene i suoi dati base (identità e posizione) e la lista dei suoi componenti.
 */
public class Infrastructure {

    private final String id;
    private final BuildingType type;
    private final int x;
    private final int y;
    private boolean active;

    // true = danneggiato da un evento: disattivato per 1 tick, manutenzione x2
    private boolean damaged;

    private final Map<Class<? extends Component>, Component> components;

    public Infrastructure(String id, BuildingType type, int x, int y) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.active = true;
        this.damaged = false;
        this.components = new HashMap<>();
    }

    /** Marca l'edificio come danneggiato e lo disattiva per questo tick. */
    public void damage() {
        this.damaged = true;
        this.active = false;
    }

    /** Chiamato da EventManager a inizio tick successivo. */
    public void resetDamage() {
        this.damaged = false;
        this.active = true;
    }

    public void addComponent(Component component) {
        this.components.put(component.getClass(), component);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }

    public boolean hasComponent(Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
    }

    public String getId()          { return id; }
    public BuildingType getType()  { return type; }
    public int getX()              { return x; }
    public int getY()              { return y; }
    public boolean isActive()      { return active; }
    public boolean isDamaged()     { return damaged; }
    public void setActive(boolean active) { this.active = active; }
}
