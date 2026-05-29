package com.cityproject.model.factory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.Component;
import com.cityproject.model.components.EnergyComponent;
import com.cityproject.model.components.FireCoverageComponent;
import com.cityproject.model.components.HealthComponent;
import com.cityproject.model.components.HousingComponent;
import com.cityproject.model.components.IncomeComponent;
import com.cityproject.model.components.MaintenanceComponent;
import com.cityproject.model.components.PollutionComponent;
import com.cityproject.model.components.EntertainmentComponent;
import com.cityproject.model.type.BuildingType;
import com.cityproject.model.type.BuildingTypeConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Factory che carica i dati dal JSON e costruisce le Infrastructure.
 * Implementa il pattern Creator in ottica Data-Driven ECS.
 * Pattern Factory
 */
public class BuildingFactory {

    private static BuildingFactory instance;
    private final Map<String, BuildingType> registry = new HashMap<>();

    private BuildingFactory() {
        loadTypesFromJson();
    }

    public static BuildingFactory getInstance() {
        if (instance == null) {
            instance = new BuildingFactory();
        }
        return instance;
    }

    private void loadTypesFromJson() {
        try {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(
                getClass().getResourceAsStream("/buildings.json")
            );

            Type listType = new TypeToken<List<BuildingTypeConfig>>() {}.getType();
            List<BuildingTypeConfig> configs = gson.fromJson(reader, listType);

            for (BuildingTypeConfig config : configs) {
                BuildingType type = new BuildingType(config.id, config.name, config.buildCost);
                
                // Convert config into base components
                if (config.housingCapacity != null) {
                    type.addBaseComponent(new HousingComponent(config.housingCapacity));
                }
                if (config.baseIncome != null) {
                    type.addBaseComponent(new IncomeComponent(config.baseIncome));
                }
                if (config.energyConsumption != null || config.energyProduction != null) {
                    int cons = config.energyConsumption != null ? config.energyConsumption : 0;
                    int prod = config.energyProduction != null ? config.energyProduction : 0;
                    type.addBaseComponent(new EnergyComponent(cons, prod));
                }
                if (config.maintenanceCost != null) {
                    type.addBaseComponent(new MaintenanceComponent(config.maintenanceCost));
                }
                if (config.pollutionGenerated != null) {
                    int rad = config.pollutionRadius != null ? config.pollutionRadius : 3;
                    type.addBaseComponent(new PollutionComponent(config.pollutionGenerated, rad));
                }
                if (config.fireCoverageRadius != null) {
                    type.addBaseComponent(new FireCoverageComponent(config.fireCoverageRadius));
                }
                if (config.healthCapacity != null) {
                    type.addBaseComponent(new HealthComponent(config.healthCapacity));
                }
                if (config.happinessBoost != null) {
                    int rad = config.entertainmentRadius != null ? config.entertainmentRadius : 3;
                    type.addBaseComponent(new EntertainmentComponent(config.happinessBoost, rad));
                }

                registry.put(type.getId(), type);
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Failed to load buildings.json: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public BuildingType getBuildingType(String typeId) {
        return registry.get(typeId);
    }

    public Infrastructure createBuilding(String typeId, int x, int y) {
        BuildingType type = registry.get(typeId);
        if (type == null) {
            throw new IllegalArgumentException("Unknown building type: " + typeId);
        }

        // Create the entity
        Infrastructure building = new Infrastructure(UUID.randomUUID().toString(), type, x, y);

        // Copy components from type to entity
        // We instantiate new ones to keep state separate per building
        for (Component baseComp : type.getBaseComponents().values()) {
            if (baseComp instanceof HousingComponent c) {
                building.addComponent(new HousingComponent(c.getCapacity()));
            } else if (baseComp instanceof IncomeComponent c) {
                building.addComponent(new IncomeComponent(c.getBaseIncome()));
            } else if (baseComp instanceof EnergyComponent c) {
                building.addComponent(new EnergyComponent(c.getConsumption(), c.getProduction()));
            } else if (baseComp instanceof MaintenanceComponent c) {
                building.addComponent(new MaintenanceComponent(c.getMaintenanceCost()));
            } else if (baseComp instanceof PollutionComponent c) {
                building.addComponent(new PollutionComponent(c.getPollutionGenerated(), c.getRadius()));
            } else if (baseComp instanceof FireCoverageComponent c) {
                building.addComponent(new FireCoverageComponent(c.getRadius()));
            } else if (baseComp instanceof HealthComponent c) {
                building.addComponent(new HealthComponent(c.getCapacity()));
            } else if (baseComp instanceof EntertainmentComponent c) {
                building.addComponent(new EntertainmentComponent(c.getHappinessBoost(), c.getRadius()));
            }
        }

        return building;
    }
}