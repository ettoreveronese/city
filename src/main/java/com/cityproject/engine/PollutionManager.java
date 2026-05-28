package com.cityproject.engine;

import com.cityproject.engine.Helpers.AreaEffectHelper;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.aspects.HasPollution;
import com.cityproject.model.policy.CityPolicy;
/**
 * Manages pollution spreading from buildings to cells.
 */
//^FINITO forse da trattare in modo diverso i parchi
public class PollutionManager {

    // city e activePolicy sono necessari per accedere alle strutture che inquinano e al modificatore di inquinamento della politica attiva
    private final CityState city;
    private final CityPolicy activePolicy;

    private static final double ATTENUATION = 0.5; //fattore di attenuazione dell'inquinamento in base alla distanza (piu è alto più l'inquinamento si riduce rapidamente con la distanza)

    
    public PollutionManager(CityState city, CityPolicy activePolicy) {
        this.city = city;
        this.activePolicy = activePolicy;
    }

    /*
    public void applyPollution() {
        // Reset pollution first 
        for (int i = 0; i < city.getRows(); i++)
            for (int j = 0; j < city.getCols(); j++)
                city.getCell(i, j).setPollution(0);

        // modifier è un moltiplicatore che riduce o aumenta l'inquinamento in base alla politica attiva
        double modifier = activePolicy.calculatePollutionModifier();

        // prendo gli edifici con l'aspetto HasPollution e per ognuno di essi aumento l'inquinamento nelle celle adiacenti in base al livello di inquinamento dell'edificio e alla distanza (più è lontana la cella, meno inquinamento riceve)
        double attenuation = 0.5; //fattore di attenuazione dell'inquinamento in base alla distanza (piu è alto più l'inquinamento si riduce rapidamente con la distanza)
        for (HasPollution polluter : city.getPollutions()) {
            Infrastructure b = (Infrastructure) polluter;
            if (!b.isActive()) continue; //applico solo se l'edificio è attivo
            
            int range = polluter.getRange(); //raggio inquinamento
            for (int dx = -range; dx <= range; dx++) { //
                for (int dy = -range; dy <= range; dy++) {
                    int nx = b.getX() + dx;
                    int ny = b.getY() + dy;
                    if (!city.isValid(nx, ny)) continue;
                    Cell cell = city.getCell(nx, ny); 

                    int distance =Math.max(Math.abs(dx) , Math.abs(dy));
                    int spread = (int)(polluter.getPollutionLevel() * modifier / (distance*attenuation + 1));
                    System.out.println(spread+"="+polluter.getPollutionLevel()+"*("+modifier+"/"+(distance*attenuation+1)+")");
                    
                    cell.setPollution(cell.getPollution() + spread);
                }
            }
        }
    }
    */
    /**
     * Instance method: applies pollution from all active polluters.
     * Resets pollution, then uses AreaEffectHelper.applyInRadius for each polluter.
     */
    public void applyPollution() {
        // Reset pollution
        for (int i = 0; i < city.getRows(); i++) {
            for (int j = 0; j < city.getCols(); j++) {
                city.getCell(i, j).setPollution(0);
            }
        }

        double modifier = activePolicy.calculatePollutionModifier();

        // For each active polluter, use applyInRadius to spread pollution
        for (HasPollution polluter : city.getPollutions()) {
            Infrastructure b = (Infrastructure) polluter;
            if (!b.isActive()) continue;//applica solo se attivo

            AreaEffectHelper.applyInRadius(city, b.getX(), b.getY(), polluter.getRange(), 
                (cell, distance) -> {
                    int spread = (int)(polluter.getPollutionLevel() * modifier / (distance*ATTENUATION + 1));
                    cell.setPollution(cell.getPollution() + spread);
                });
        }
    }
}
