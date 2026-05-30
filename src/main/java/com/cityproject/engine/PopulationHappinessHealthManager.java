package com.cityproject.engine;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.HousingComponent;

/**
 * Manages local/global happiness, health AND population each tick.
 */
public class PopulationHappinessHealthManager {

    private static final double BASE_HAPPINESS = 100.0;
    private static final double POLLUTION_HAPPINESS_PENALTY = 2.0;
    private static final double POLLUTION_HEALTH_PENALTY    = 0.7;

    private final CityState city;

    public PopulationHappinessHealthManager(CityState city) {
        this.city = city;
    }

    public void applyPopulationHappinessHealth() {
        updateLocalValues();
        updatePopulation();
        updateGlobalValues();
    }

    private void updateLocalValues() {
        double baseHealth = 100.0;
        
        int population = city.getPopulation();
        if (population > 0) {
            // La salute base è influenzata dalla capacità sanitaria totale rispetto alla popolazione, con un massimo di 100 e un minimo di 50 (se la capacità sanitaria è insufficiente)
            double ratio = (double) city.getTotalHealthCapacity() / population;
            baseHealth = 25.0 + Math.min(1.0, ratio) * 75.0; // se la capacità sanitaria è pari alla popolazione o superiore, la salute base è 100, altrimenti scende fino a un minimo di 25 quando la capacità è molto bassa rispetto alla popolazione.
        }

        for (Infrastructure b : city.getHousingBuildings()) {
            HousingComponent r = b.getComponent(HousingComponent.class);

            //se l'edificio è inattivo, la felicità è 0 e la salute è dimezzata (simulando condizioni di degrado)
            if (!b.isActive()) {
                r.setLocalHappiness(0);
                r.setLocalHealth(r.getLocalHealth() / 2.0);
                continue;
            }

            double happiness = BASE_HAPPINESS;
            double health    = baseHealth;

            int pollution = city.getCell(b.getX(), b.getY()).getPollution();
            happiness -= pollution * POLLUTION_HAPPINESS_PENALTY;
            health    -= pollution * POLLUTION_HEALTH_PENALTY;
            
            int entertainment = city.getCell(b.getX(), b.getY()).getEntertainmentBonus();
            happiness += entertainment;

            r.setLocalHappiness(happiness);
            r.setLocalHealth(health);
        }
    }

    // La popolazione è la somma delle capacità abitative di tutti gli edifici residenziali attivi, è quello che vineen guardato per il sistema di livellamento
    private void updatePopulation() {
        int total = 0; 
        for (Infrastructure b : city.getHousingBuildings()) {
            HousingComponent h = b.getComponent(HousingComponent.class);
            if (b.isActive()) {
                total += h.getCapacity();
            }
        }
        city.setPopulation(total);
    }

    //Felicità e salute globali sono medie ponderate delle felicità e salute locali, pesate per la capacità abitativa
    private void updateGlobalValues() {
        double sumPH = 0, sumPS = 0, sumP = 0;
        for (Infrastructure b : city.getHousingBuildings()) {
            HousingComponent r = b.getComponent(HousingComponent.class);
            sumPH += (double) r.getCapacity() * r.getLocalHappiness(); // peso la felicità locale per la capacità abitativa
            sumPS += (double) r.getCapacity() * r.getLocalHealth(); // peso la salute locale per la capacità abitativa
            sumP  += r.getCapacity(); // sommo la capacità abitativa totale anche delle abitazioni inattive (per intaccare i valori globali) per fare la media ponderata
        }
        if (sumP > 0) {
            city.setGlobalHappiness(sumPH / sumP);
            city.setGlobalHealth(sumPS / sumP);
        }
    }
}