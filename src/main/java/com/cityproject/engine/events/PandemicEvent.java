package com.cityproject.engine.events;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.components.HealthComponent;
import com.cityproject.model.components.IncomeComponent;
import com.cityproject.model.components.MaintenanceComponent;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Evento pandemia: 3 livelli determinati dalla salute globale.
 *
 * Livello 1 (health > 37.5): -10 felicità, fabbriche -30% reddito, sanità +30% manutenzione
 * Livello 2 (25 < health ≤ 37.5): -25 felicità, fabbriche -30% reddito, sanità +50% manutenzione
 * Livello 3 (health ≤ 25):        -45 felicità, fabbriche -30% reddito, sanità +100% manutenzione
 *
 * Dura 1 tick. EventManager chiama reset() all'inizio del tick successivo.
 */
public class PandemicEvent extends CityEvent {

    // ── Soglie salute ─────────────────────────────────────────────────────────
    private static final double HEALTH_THRESHOLD_L2 = 37.5;
    private static final double HEALTH_THRESHOLD_L3 = 25.0;

    // ── Effetti per livello [0 non usato, 1, 2, 3] ───────────────────────────
    private static final double[] HAPPINESS_PENALTY      = { 0, -10, -25, -45 };
    private static final double[] MAINTENANCE_MULTIPLIER = { 0, 1.3, 1.5, 2.0 };
    private static final double   PRODUCTIVITY_MULTIPLIER = 0.70; // -30%

    // Valori originali da ripristinare — coppie (componente, valore originale)
    private final List<Map.Entry<IncomeComponent, Integer>>      affectedIncome      = new ArrayList<>();
    private final List<Map.Entry<MaintenanceComponent, Integer>> affectedMaintenance = new ArrayList<>();

    private int level;

    @Override
    public void apply(CityState city) {
        double health = city.getGlobalHealth();

        if (health <= HEALTH_THRESHOLD_L3)      level = 3;
        else if (health <= HEALTH_THRESHOLD_L2) level = 2;
        else                                     level = 1;

        // Penalità felicità globale
        city.setGlobalHappiness(city.getGlobalHappiness() + HAPPINESS_PENALTY[level]);

        // Effetti su edifici
        for (Infrastructure b : city.getBuildings()) {
            // Fabbriche (hanno IncomeComponent e producono inquinamento) → -30% reddito
            if (b.hasComponent(IncomeComponent.class) && !b.hasComponent(HealthComponent.class)) {
                IncomeComponent ic = b.getComponent(IncomeComponent.class);
                int original = ic.getBaseIncome();
                ic.setBaseIncome((int)(original * PRODUCTIVITY_MULTIPLIER));
                affectedIncome.add(new AbstractMap.SimpleEntry<>(ic, original));
            }

            // Strutture sanitarie (hanno HealthComponent) → aumento manutenzione
            if (b.hasComponent(HealthComponent.class) && b.hasComponent(MaintenanceComponent.class)) {
                MaintenanceComponent mc = b.getComponent(MaintenanceComponent.class);
                int original = mc.getMaintenanceCost();
                mc.setMaintenanceCost((int)(original * MAINTENANCE_MULTIPLIER[level]));
                affectedMaintenance.add(new AbstractMap.SimpleEntry<>(mc, original));
            }
        }

        System.out.println("[PANDEMIC] Level " + level
                + " | happiness " + HAPPINESS_PENALTY[level]
                + " | health=" + String.format("%.1f", health));
    }

    /** Ripristina tutti i valori modificati. Chiamato da EventManager al tick successivo. */
    public void reset() {
        for (Map.Entry<IncomeComponent, Integer> e : affectedIncome)
            e.getKey().setBaseIncome(e.getValue());
        for (Map.Entry<MaintenanceComponent, Integer> e : affectedMaintenance)
            e.getKey().setMaintenanceCost(e.getValue());

        affectedIncome.clear();
        affectedMaintenance.clear();
    }

    public int getLevel() { return level; }
}
