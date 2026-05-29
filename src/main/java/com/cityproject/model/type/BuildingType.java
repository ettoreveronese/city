package com.cityproject.model.type;

import com.cityproject.model.components.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Type Object immutabile che rappresenta la configurazione di base di un edificio.
 * Viene caricato dai dati JSON.
 */
public class BuildingType {
    private String id;
    private String name;
    private int buildCost;
    
    // Configurazione iniziale dei componenti
    private Map<Class<? extends Component>, Component> baseComponents;

    public BuildingType() {
        this.baseComponents = new HashMap<>();
    }

    public BuildingType(String id, String name, int buildCost) {
        this.id = id;
        this.name = name;
        this.buildCost = buildCost;
        this.baseComponents = new HashMap<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getBuildCost() { return buildCost; }
    public void setBuildCost(int buildCost) { this.buildCost = buildCost; }

    public void addBaseComponent(Component component) {
        this.baseComponents.put(component.getClass(), component);
    }

    public Map<Class<? extends Component>, Component> getBaseComponents() {
        return baseComponents;
    }
}
