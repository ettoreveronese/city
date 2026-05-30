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

    private final String id;       // Identificatore univoco dell'istanza
    private final BuildingType type; // Tipo condiviso letto dal JSON
    private final int x;
    private final int y;
    private boolean active;
    
    // Mappa dei componenti, indicizzata per classe per accesso O(1)
    private final Map<Class<? extends Component>, Component> components;

    public Infrastructure(String id, BuildingType type, int x, int y) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.active = true;
        this.components = new HashMap<>();
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

    // --- Getters ---
    public String getId() { return id; }
    public BuildingType getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}