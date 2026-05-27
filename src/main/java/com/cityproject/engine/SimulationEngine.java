package com.cityproject.engine;

import com.cityproject.model.CityState;
import com.cityproject.model.policy.CityPolicy;

/**
 * The core simulation engine. Executes one "tick" at a time.
 * Follows exactly the 6-step sequence defined in the bibbia.
 * GRASP Controller: coordinates all simulation logic without doing calculations itself.
 */
public class SimulationEngine {

    private final CityState city;
    private CityPolicy activePolicy;

    public SimulationEngine(CityState city, CityPolicy initialPolicy) {
        this.city = city;
        this.activePolicy = initialPolicy;
    }

    /** Swap the active policy at runtime — Strategy Pattern in action */
    public void setPolicy(CityPolicy policy) { this.activePolicy = policy; }
    public CityPolicy getActivePolicy()       { return activePolicy; }

    /**
     * Advance the simulation by one tick.
     * Steps follow the bibbia exactly:
     * 1. Check road destruction
     * 2. Energy balance → deactivate buildings if deficit
     * 3. Apply pollution to cells
     * 4. Apply happiness/health (local + global)
     * 5. Income and maintenance
     * 6. Events (fire, covid, tower collapse)
     */
    public void tick() {
        city.incrementTick();

        // delegate to managers
        RoadConnectionManager rcManager = new RoadConnectionManager(city);
        EnergyBalanceManager ebManager = new EnergyBalanceManager(city);
        PollutionManager pManager = new PollutionManager(city, activePolicy);
        HappinessHealthManager hhManager = new HappinessHealthManager(city);
        BudgetManager bManager = new BudgetManager(city, activePolicy);
        EventManager eManager = new EventManager(city);

        rcManager.checkConnections();
        ebManager.balanceEnergy();
        pManager.applyPollution();
        hhManager.applyHappinessAndHealth();
        bManager.processIncomeAndMaintenance();
        eManager.processEvents();

        // Notify all observers (Dashboard, Logger, etc.)
        city.notifyObservers();
    }

    // Legacy: step methods removed. Logic is now delegated to dedicated manager classes:
    // RoadConnectionManager, EnergyBalanceManager, PollutionManager,
    // HappinessHealthManager, BudgetManager, EventManager.
}


// /**
//  * The core simulation engine. Executes one "tick" at a time.
//  * Follows exactly the 6-step sequence defined in the bibbia.
//  * GRASP Controller: coordinates all simulation logic without doing calculations itself.
//  */

    
// package com.cityproject.engine;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.HashSet;
// import java.util.LinkedList;
// import java.util.List;
// import java.util.Queue;
// import java.util.Set;

// import com.cityproject.model.Cell;
// import com.cityproject.model.CityState;
// import com.cityproject.model.Infrastructure;
// import com.cityproject.model.aspects.HasEnergy;
// import com.cityproject.model.aspects.HasMaintenance;
// import com.cityproject.model.aspects.HasPollution;
// import com.cityproject.model.buildings.Residence;
// import com.cityproject.model.buildings.Road;
// import com.cityproject.model.policy.CityPolicy;

// /**
//  * The core simulation engine. Executes one "tick" at a time.
//  * Follows exactly the 6-step sequence defined in the bibbia.
//  * GRASP Controller: coordinates all simulation logic without doing calculations itself.
//  */
// public class SimulationEngine {

//     private final CityState city;
//     private CityPolicy activePolicy;

//     public SimulationEngine(CityState city, CityPolicy initialPolicy) {
//         this.city = city;
//         this.activePolicy = initialPolicy;
//     }

//     /** Swap the active policy at runtime — Strategy Pattern in action */
//     public void setPolicy(CityPolicy policy) { this.activePolicy = policy; }
//     public CityPolicy getActivePolicy()       { return activePolicy; }

//     /**
//      * Advance the simulation by one tick.
//      * Steps follow the bibbia exactly:
//      * 1. Check road destruction
//      * 2. Energy balance → deactivate buildings if deficit
//      * 3. Apply pollution to cells
//      * 4. Apply happiness/health (local + global)
//      * 5. Income and maintenance
//      * 6. Events (fire, covid, tower collapse)
//      */
//     public void tick() {
//         city.incrementTick();

//         step1_checkRoadConnections();
//         step2_energyBalance();
//         step3_applyPollution();
//         step4_applyHappinessAndHealth();
//         step5_incomeAndMaintenance();
//         step6_events();

//         // Notify all observers (Dashboard, Logger, etc.)
//         city.notifyObservers();
//     }

//     // -------------------------------------------------------------------------
//     // STEP 1 — Road connection check (VCS algorithm from bibbia)
//     // -------------------------------------------------------------------------
//     private void step1_checkRoadConnections() {
//         // Find root road (Strada-Radice at map center)
//         Road rootRoad = findRootRoad();
//         if (rootRoad == null) return;

//         // BFS from root road to find all connected roads
//         Set<String> connectedRoadIds = bfsConnectedRoads(rootRoad);

//         // A building is connected if at least one adjacent cell has a connected road
//         for (Infrastructure b : city.getBuildings()) {
//             if (b instanceof Road) continue; // roads don't need to check themselves
//             boolean connected = isAdjacentToConnectedRoad(b, connectedRoadIds);
//             b.setActive(connected);
//         }
//     }

//     private Road findRootRoad() {
//         for (Infrastructure b : city.getBuildings())
//             if (b instanceof Road r && r.isRoot()) return r;
//         return null;
//     }

//     private Set<String> bfsConnectedRoads(Road root) {
//         Set<String> visited = new HashSet<>();
//         Queue<Road> queue = new LinkedList<>();
//         queue.add(root);
//         visited.add(root.getId());

//         while (!queue.isEmpty()) {
//             Road current = queue.poll();
//             // Check 4 adjacent cells for other roads
//             int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
//             for (int[] d : dirs) {
//                 int nx = current.getX() + d[0];
//                 int ny = current.getY() + d[1];
//                 if (!city.isValid(nx, ny)) continue;
//                 Infrastructure neighbor = city.getCell(nx, ny).getStructure();
//                 if (neighbor instanceof Road r && !visited.contains(r.getId())) {
//                     visited.add(r.getId());
//                     queue.add(r);
//                 }
//             }
//         }
//         return visited;
//     }

//     private boolean isAdjacentToConnectedRoad(Infrastructure b, Set<String> connectedRoads) {
//         // Check cells adjacent to the building
//         int[] dx = {-1, 0, 1, 0};
//         int[] dy = {0, -1, 0, 1};
//         for (int i = 0; i < 4; i++) {
//             int nx = b.getX() + dx[i];
//             int ny = b.getY() + dy[i];
//             if (!city.isValid(nx, ny)) continue;
//             Infrastructure s = city.getCell(nx, ny).getStructure();
//             if (s instanceof Road && connectedRoads.contains(s.getId())) return true;
//         }
//         return false;
//     }

//     // -------------------------------------------------------------------------
//     // STEP 2 — Energy balance
//     // -------------------------------------------------------------------------
//     private void step2_energyBalance() {
//         int totalProduced = 0;
//         int totalConsumed = 0;

//         for (Infrastructure b : city.getBuildings()) {
//             if (!b.isActive()) continue;
//             if (b instanceof HasEnergy e) totalProduced += e.getEnergyProduced();
//             totalConsumed += b.getEnergyConsumption();
//         }

//         // If deficit: randomly deactivate non-road buildings until balanced
//         // (bibbia: "spegni randomicamente abbastanza strutture fino a coprire")
//         if (totalConsumed > totalProduced) {
//             int deficit = totalConsumed - totalProduced;
//             List<Infrastructure> candidates = new ArrayList<>();
//             for (Infrastructure b : city.getBuildings())
//                 if (b.isActive() && !(b instanceof Road)) candidates.add(b);

//             Collections.shuffle(candidates);
//             for (Infrastructure b : candidates) {
//                 if (deficit <= 0) break;
//                 deficit -= b.getEnergyConsumption();
//                 b.setActive(false);
//             }
//         }
//     }

//     // -------------------------------------------------------------------------
//     // STEP 3 — Apply pollution to cells
//     // -------------------------------------------------------------------------
//     private void step3_applyPollution() {
//         // Reset pollution first
//         for (int i = 0; i < city.getRows(); i++)
//             for (int j = 0; j < city.getCols(); j++)
//                 city.getCell(i, j).setPollution(0);

//         // Apply pollution modifier from active policy
//         double modifier = activePolicy.calculatePollutionModifier();

//         for (Infrastructure b : city.getBuildings()) {
//             if (!b.isActive()) continue;
//             if (b instanceof HasPollution p) {
//                 int range = p.getRange();
//                 for (int dx = -range; dx <= range; dx++) {
//                     for (int dy = -range; dy <= range; dy++) {
//                         int nx = b.getX() + dx;
//                         int ny = b.getY() + dy;
//                         if (!city.isValid(nx, ny)) continue;
//                         Cell cell = city.getCell(nx, ny);
//                         int distance = Math.abs(dx) + Math.abs(dy);
//                         int spread = (int)(p.getPollutionLevel() * modifier / (distance + 1));
//                         cell.setPollution(cell.getPollution() + spread);
//                     }
//                 }
//             }
//         }
//     }

//     // -------------------------------------------------------------------------
//     // STEP 4 — Happiness and health (local + global weighted average)
//     // -------------------------------------------------------------------------
//     private void step4_applyHappinessAndHealth() {
//         // Reset fire protection each tick (FireStation.applyEffects will reset it)
//         for (int i = 0; i < city.getRows(); i++)
//             for (int j = 0; j < city.getCols(); j++)
//                 city.getCell(i, j).setFireProtection(false);

//         // Apply all building effects (residences update local happiness/health,
//         // fire stations set fire protection, natural areas reduce pollution, etc.)
//         for (Infrastructure b : city.getBuildings())
//             b.applyEffects(city);

//         // Compute global happiness as weighted average over residences (bibbia formula)
//         // globalHappiness = Σ(pi * fi) / Σpi
//         double sumPF = 0, sumPS = 0, sumP = 0;
//         for (Infrastructure b : city.getBuildings()) {
//             if (b instanceof Residence r && r.isActive()) {
//                 sumPF += r.getCapacity() * r.getLocalHappiness();
//                 sumPS += r.getCapacity() * r.getLocalHealth();
//                 sumP  += r.getCapacity();
//             }
//         }
//         if (sumP > 0) {
//             city.setGlobalHappiness(sumPF / sumP);
//             city.setGlobalHealth(sumPS / sumP);
//         }
//     }

//     // -------------------------------------------------------------------------
//     // STEP 5 — Income and maintenance
//     // -------------------------------------------------------------------------
//     private void step5_incomeAndMaintenance() {
//         // Tax income from active policy
//         city.setBudget(city.getBudget() + activePolicy.calculateTax(city));
//         activePolicy.applyPolicyEffects(city);

//         // Subtract maintenance costs
//         for (Infrastructure b : city.getBuildings()) {
//             if (b instanceof HasMaintenance m) {
//                 int cost = b.isActive() ? m.getMaintenanceCost() : m.getMaintenanceCost() * 2;
//                 city.setBudget(city.getBudget() - cost);
//             }
//         }
//     }

//     // -------------------------------------------------------------------------
//     // STEP 6 — Random events
//     // -------------------------------------------------------------------------
//     private void step6_events() {
//         fireEvent();
//         // TODO: add CovidEvent, TowerCollapseEvent in next iteration
//     }

//     private void fireEvent() {
//         // Fire probability increases with number of buildings (bibbia)
//         long buildingCount = city.getBuildings().stream()
//             .filter(b -> !(b instanceof Road)).count();
//         double fireProbability = buildingCount * 0.002; // tunable

//         if (Math.random() > fireProbability) return;

//         // Pick a random non-road, non-root structure
//         List<Infrastructure> targets = new ArrayList<>();
//         for (Infrastructure b : city.getBuildings())
//             if (!(b instanceof Road) && b.isActive()) targets.add(b);

//         if (targets.isEmpty()) return;

//         Infrastructure target = targets.get((int)(Math.random() * targets.size()));
//         Cell targetCell = city.getCell(target.getX(), target.getY());

//         if (targetCell.hasFireProtection()) {
//             // Fire station saves it — only this building is destroyed
//             System.out.println("[EVENT] Fire at " + target.getId() + " — contained by fire station.");
//             city.removeBuilding(target);
//             targetCell.setStructure(null);
//         } else {
//             // No protection — destroy all adjacent buildings too (bibbia)
//             System.out.println("[EVENT] Fire at " + target.getId() + " — spreading!");
//             destroyWithNeighbors(target);
//         }
//     }

//     private void destroyWithNeighbors(Infrastructure origin) {
//         int range = 1;
//         for (int dx = -range; dx <= range; dx++) {
//             for (int dy = -range; dy <= range; dy++) {
//                 int nx = origin.getX() + dx;
//                 int ny = origin.getY() + dy;
//                 if (!city.isValid(nx, ny)) continue;
//                 Cell cell = city.getCell(nx, ny);
//                 Infrastructure s = cell.getStructure();
//                 if (s != null && !(s instanceof Road)) {
//                     city.removeBuilding(s);
//                     cell.setStructure(null);
//                 }
//             }
//         }
//     }
// }

