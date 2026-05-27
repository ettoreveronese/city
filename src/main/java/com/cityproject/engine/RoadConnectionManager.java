package com.cityproject.engine;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.buildings.Road;

/**
 * Handles Road Connection Verification (VCS algorithm).
 * Buildings are active only if connected to the root road via roads.
 */
public class RoadConnectionManager {

    private final CityState city;

    public RoadConnectionManager(CityState city) {
        this.city = city;
    }

    public void checkConnections() {
        // parto dalla strada radice e faccio un BFS per trovare tutte le strade connesse a essa, poi disattivo gli edifici non adiacenti a strade connesse
        Road rootRoad = findRootRoad();
        if (rootRoad == null) return; //todo dovrebbe dare errore, per debugging, con eccezione (anche nel log del gioco)

        Set<String> connectedRoadIds = bfsConnectedRoads(rootRoad);

        // Attivo solo gli edifici adiacenti a strade connesse, disattivo gli altri
        for (Infrastructure b : city.getBuildings()) {
            if (b instanceof Road) continue; // salto le strade
            boolean connected = isAdjacentToConnectedRoad(b, connectedRoadIds); //se b è adiacente a una strada connessa, allora è connesso
            b.setActive(connected);
        }
    }



    //METODI A SUPPORTO DELL'ALGORITMO VCS (Verifica Connessione Stradale)
    
    private Road findRootRoad() {
        for (Infrastructure b : city.getBuildings())
            if (b instanceof Road r && r.isRoot()) return r;
        return null;
    }//così se cambiamo idea sulla sua posizione, basta aggiornare questo metodo senza toccare il resto del codice

    //eseguo un BFS trattando le strade come nodi e le adiacenze come archi, partendo dalla strada radice, per trovare solo le strade connesse a essa
    private Set<String> bfsConnectedRoads(Road root) {
        
        Set<String> visited = new HashSet<>();       // Dizionario per tenere traccia delle strade connesse al root
        Queue<Road> queue = new LinkedList<>();     // Coda per BFS
        queue.add(root);
        visited.add(root.getId());

        while (!queue.isEmpty()) {
            Road current = queue.poll();
            // Check 4 adjacent cells for other roads
            int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}}; // right, left, down, up
            for (int[] d : dirs) {

                int nx = current.getX() + d[0];
                int ny = current.getY() + d[1];

                if (!city.isValid(nx, ny)) continue; // controllo che non esca dai confini della mappa

                Infrastructure neighbor = city.getCell(nx, ny).getStructure(); // prendo la struttura presente nella cella adiacente
                if (neighbor instanceof Road r && !visited.contains(r.getId())) { // se è una strada e non è già stata visitata
                    visited.add(r.getId()); // la aggiungo al dizionario delle strade connesse
                    queue.add(r);           // e la metto in coda per esplorare le sue adiacenze
                }
            }
        }
        return visited; //^ ritorno l'insieme degli id delle strade connesse al root
    }

    // Un edificio è connesso se almeno una cella adiacente ha una strada connessa
    private boolean isAdjacentToConnectedRoad(Infrastructure b, Set<String> connectedRoads) {
        int[] dx = {-1, 0, 1, 0}; // left, down, right, up
        int[] dy = {0, -1, 0, 1}; // left, down, right, up
        for (int i = 0; i < 4; i++) {
            int nx = b.getX() + dx[i];
            int ny = b.getY() + dy[i];
            if (!city.isValid(nx, ny)) continue; // controllo che non esca dai confini della mappa
            Infrastructure s = city.getCell(nx, ny).getStructure(); // prendo la struttura presente nella cella adiacente
            if (s instanceof Road && connectedRoads.contains(s.getId())) return true; // se è una strada e fa parte delle strade connesse, allora l'edificio è connesso
        }
        return false;
    }
}
