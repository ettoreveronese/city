package com.cityproject.engine.events;

import com.cityproject.model.Cell;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Evento incendio con due livelli di gravità.
 *
 * LIEVE: distrugge il bersaglio.
 *        Senza fireProtection → danneggia i vicini (distanza 1, 8 direzioni).
 *
 * GRAVE: distrugge il bersaglio.
 *        Con fireProtection    → vicini solo danneggiati, distanza 2 immuni.
 *        Senza fireProtection  → distrugge vicini (dist 1) + danneggia dist 2.
 *
 * "Danneggiato" = disattivato per 1 tick + manutenzione x2 (gestita da BudgetManager).
 * Le strade non vengono mai selezionate come bersaglio né distrutte come collaterali.
 */
public class FireEvent extends CityEvent {

    // ── Probabilità dado 2 ────────────────────────────────────────────────────
    private static final double P_LIEVE = 0.70;

    public enum FireLevel { LIEVE, GRAVE }

    private final FireLevel level;
    private final Random random = new Random();

    public FireEvent() {
        this.level = (Math.random() < P_LIEVE) ? FireLevel.LIEVE : FireLevel.GRAVE;
    }

    public FireLevel getLevel() { return level; }

    @Override
    public void apply(CityState city) {
        // Seleziona bersaglio: edificio attivo, non strada
        List<Infrastructure> candidates = new ArrayList<>();
        for (Infrastructure b : city.getBuildings())
            if (b.isActive() && !b.getType().isRoad()) candidates.add(b);

        if (candidates.isEmpty()) return;

        Infrastructure target = candidates.get(random.nextInt(candidates.size()));
        int cx = target.getX();
        int cy = target.getY();
        boolean protected_ = city.getCell(cx, cy).hasFireProtection();

        // Distrugge sempre il bersaglio primario
        destroyBuilding(city, target);
        System.out.println("[FIRE " + level + "] Destroyed " + target.getId()
                + " at (" + cx + "," + cy + ")"
                + (protected_ ? " [fire protected]" : ""));

        if (level == FireLevel.LIEVE) {
            if (!protected_) {
                damageRing(city, cx, cy, 1);
            }

        } else { // GRAVE
            if (protected_) {
                damageRing(city, cx, cy, 1);
            } else {
                destroyRing(city, cx, cy, 1);
                damageRing(city, cx, cy, 2);
            }
        }
    }

    // ── Helper: distrugge tutti gli edifici non-strada a distanza esatta dist ──
    private void destroyRing(CityState city, int cx, int cy, int dist) {
        for (Infrastructure b : getBuildingsAtDistance(city, cx, cy, dist)) {
            destroyBuilding(city, b);
            System.out.println("[FIRE] Collateral destroy: " + b.getId());
        }
    }

    // ── Helper: danneggia tutti gli edifici non-strada a distanza esatta dist ──
    private void damageRing(CityState city, int cx, int cy, int dist) {
        for (Infrastructure b : getBuildingsAtDistance(city, cx, cy, dist)) {
            b.damage();
            System.out.println("[FIRE] Collateral damage: " + b.getId());
        }
    }

    // ── Helper: rimuove un edificio dalla griglia e dalla lista ──────────────
    private void destroyBuilding(CityState city, Infrastructure b) {
        city.removeBuilding(b);
        Cell cell = city.getCell(b.getX(), b.getY());
        if (cell != null) cell.setStructure(null);
    }

    // ── Helper: restituisce edifici non-strada a distanza Chebyshev esatta ────
    private List<Infrastructure> getBuildingsAtDistance(CityState city, int cx, int cy, int dist) {
        List<Infrastructure> result = new ArrayList<>();
        for (int dx = -dist; dx <= dist; dx++) {
            for (int dy = -dist; dy <= dist; dy++) {
                if (Math.max(Math.abs(dx), Math.abs(dy)) != dist) continue;
                int nx = cx + dx;
                int ny = cy + dy;
                if (!city.isValid(nx, ny)) continue;
                Infrastructure s = city.getCell(nx, ny).getStructure();
                if (s != null && !s.getType().isRoad()) result.add(s);
            }
        }
        return result;
    }
}
