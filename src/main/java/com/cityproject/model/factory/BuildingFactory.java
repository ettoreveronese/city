package com.cityproject.model.factory;

import com.cityproject.model.Infrastructure;
import com.cityproject.model.buildings.CleanPowerPlant;
import com.cityproject.model.buildings.DirtyPowerPlant;
import com.cityproject.model.buildings.FireStation;
import com.cityproject.model.buildings.HealthBuilding;
import com.cityproject.model.buildings.NaturalArea;
import com.cityproject.model.buildings.ProductionBuilding;
import com.cityproject.model.buildings.Residence;
import com.cityproject.model.buildings.Road;

/**
 * FACTORY PATTERN.
 * The only place in the codebase where concrete building classes are instantiated.
 * SimulationEngine and controllers never use "new XBuilding()" directly —
 * they always go through this factory.
 * This means: adding a new building type only requires changing this file.
 */
public class BuildingFactory {

    private static int idCounter = 0;

    /**
     * Creates a building of the given type at position (x, y).
     * @param type  string key, e.g. "COTTAGE", "COAL", "PARK"
     * @param x     grid row
     * @param y     grid column
     * @return      the correct Infrastructure subclass instance
     */
    public static Infrastructure create(String type, int x, int y) {
        String id = type.toUpperCase() + "_" + (idCounter++);

        return switch (type.toUpperCase()) {
            // Residences
            case "COTTAGE"       -> new Residence(id, x, y, Residence.Type.COTTAGE);
            case "CONDO"         -> new Residence(id, x, y, Residence.Type.CONDO);
            case "SKYSCRAPER"    -> new Residence(id, x, y, Residence.Type.SKYSCRAPER);
            // Production
            case "FOOD"          -> new ProductionBuilding(id, x, y, ProductionBuilding.Type.FOOD);
            case "METALLURGICAL" -> new ProductionBuilding(id, x, y, ProductionBuilding.Type.METALLURGICAL);
            case "PETROCHEMICAL" -> new ProductionBuilding(id, x, y, ProductionBuilding.Type.PETROCHEMICAL);
            // Clean power
            case "SOLAR"         -> new CleanPowerPlant(id, x, y, CleanPowerPlant.Type.SOLAR);
            case "WIND"          -> new CleanPowerPlant(id, x, y, CleanPowerPlant.Type.WIND);
            case "NUCLEAR"       -> new CleanPowerPlant(id, x, y, CleanPowerPlant.Type.NUCLEAR);
            // Dirty power
            case "COAL"          -> new DirtyPowerPlant(id, x, y, DirtyPowerPlant.Type.COAL);
            case "OIL"           -> new DirtyPowerPlant(id, x, y, DirtyPowerPlant.Type.OIL);
            case "INCINERATOR"   -> new DirtyPowerPlant(id, x, y, DirtyPowerPlant.Type.INCINERATOR);
            // Nature
            case "PARK"          -> new NaturalArea(id, x, y, NaturalArea.Type.PARK);
            case "RESERVE"       -> new NaturalArea(id, x, y, NaturalArea.Type.NATURE_RESERVE);
            case "NATIONAL_PARK" -> new NaturalArea(id, x, y, NaturalArea.Type.NATIONAL_PARK);
            // Health
            case "CLINIC"        -> new HealthBuilding(id, x, y, HealthBuilding.Type.CLINIC);
            case "HOSPITAL"      -> new HealthBuilding(id, x, y, HealthBuilding.Type.HOSPITAL);
            // Fire
            case "FIRE_STATION"  -> new FireStation(id, x, y);
            // Roads
            case "ROAD"          -> new Road(id, x, y, false);
            case "ROOT_ROAD"     -> new Road(id, x, y, true);

            default -> throw new IllegalArgumentException("Unknown building type: " + type);
        };
    }
}