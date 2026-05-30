package com.cityproject.engine;

import com.cityproject.engine.events.FireEvent;
import com.cityproject.engine.events.PandemicEvent;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce gli eventi casuali ogni tick.
 *
 * Dado 1 — tipo di evento:
 *   [0, P_FIRE)                  → FireEvent  (dado 2 determina lieve/grave internamente)
 *   [P_FIRE, P_FIRE+P_PANDEMIC)  → PandemicEvent
 *   resto                        → niente
 *
 * La pandemia non si sovrappone: se una è attiva non ne parte un'altra.
 * I danni temporanei (damaged) vengono resettati all'inizio del tick successivo.
 */
public class EventManager {

    // ── Probabilità dado 1 ────────────────────────────────────────────────────
    private static final double P_FIRE     = 0.10;
    private static final double P_PANDEMIC = 0.10;

    private final CityState city;

    private PandemicEvent  activePandemic  = null;          // pandemia in corso
    private final List<Infrastructure> damagedBuildings = new ArrayList<>(); // edifici danneggiati da resettare

    public EventManager(CityState city) {
        this.city = city;
    }

    public void processEvents() {
        // 1. Reset danni e pandemia del tick precedente
        resetDamagedBuildings();
        resetPandemic();

        // 2. Tira dado 1
        double roll = Math.random();

        if (roll < P_FIRE) {
            FireEvent fire = new FireEvent();
            fire.apply(city);
            // Registra edifici danneggiati per il reset al prossimo tick
            for (Infrastructure b : city.getBuildings())
                if (b.isDamaged()) damagedBuildings.add(b);

        } else if (roll < P_FIRE + P_PANDEMIC) {
            PandemicEvent pandemic = new PandemicEvent();
            pandemic.apply(city);
            activePandemic = pandemic;
        }
        // else: niente
    }

    private void resetDamagedBuildings() {
        for (Infrastructure b : damagedBuildings)
            b.resetDamage();
        damagedBuildings.clear();
    }

    private void resetPandemic() {
        if (activePandemic != null) {
            activePandemic.reset();
            activePandemic = null;
        }
    }

    public boolean isPandemicActive() { return activePandemic != null; }
}
