package com.cityproject.engine.persistence;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.factory.BuildingFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveGame(CityState city, String policyName, String filePath) {
        SaveGameDTO dto = new SaveGameDTO();
        dto.rows = city.getRows();
        dto.cols = city.getCols();
        dto.budget = city.getBudget();
        dto.population = city.getPopulation();
        dto.tick = city.getTick();
        dto.globalHappiness = city.getGlobalHappiness();
        dto.globalHealth = city.getGlobalHealth();
        dto.activePolicyName = policyName;
        
        List<SavedBuildingDTO> bList = new ArrayList<>();
        for (Infrastructure b : city.getBuildings()) {
            SavedBuildingDTO bd = new SavedBuildingDTO();
            bd.typeId = b.getType().getId();
            bd.x = b.getX();
            bd.y = b.getY();
            bd.isActive = b.isActive();
            bList.add(bd);
        }
        dto.buildings = bList;

        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(dto, writer);
            System.out.println("Partita salvata con successo in: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LoadedGame loadGame(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            SaveGameDTO dto = gson.fromJson(reader, SaveGameDTO.class);
            if (dto == null) return null;

            CityState city = new CityState(dto.rows, dto.cols, dto.budget);
            city.setPopulation(dto.population);
            city.setTick(dto.tick);
            city.setGlobalHappiness(dto.globalHappiness);
            city.setGlobalHealth(dto.globalHealth);

            for (SavedBuildingDTO bd : dto.buildings) {
                Infrastructure infra = BuildingFactory.getInstance().createBuilding(bd.typeId, bd.x, bd.y);
                infra.setActive(bd.isActive);
                city.addBuilding(infra);
                city.getCell(bd.x, bd.y).setStructure(infra);
            }

            return new LoadedGame(city, dto.activePolicyName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class LoadedGame {
        public final CityState city;
        public final String policyName;
        public LoadedGame(CityState city, String policyName) {
            this.city = city;
            this.policyName = policyName;
        }
    }
}
