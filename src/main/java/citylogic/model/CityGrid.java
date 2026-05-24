package citylogic.model;

import citylogic.model.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Griglia logica della città (NxM celle).
 */
public class CityGrid {

    private final int width;
    private final int height;
    private final Cell[][] cells;

    public CityGrid(int width, int height) {
        this.width  = width;
        this.height = height;
        this.cells  = new Cell[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                cells[x][y] = new Cell(x, y);
    }

    public Cell getCell(int x, int y) {
        if (!inBounds(x, y)) return null;
        return cells[x][y];
    }

    public void setEntity(int x, int y, Entity entity) {
        if (inBounds(x, y)) cells[x][y].setEntity(entity);
    }

    public void removeEntity(int x, int y) {
        if (inBounds(x, y)) cells[x][y].setEntity(null);
    }

    /**
     * Restituisce le celle a esattamente la distanza Chebyshev specificata
     * (es. distanza=1 → 8 vicini diretti, distanza=2 → ring esterno 5x5).
     */
    public List<Cell> getCellsAtDistance(int cx, int cy, int distance) {
        List<Cell> result = new ArrayList<>();
        for (int dx = -distance; dx <= distance; dx++) {
            for (int dy = -distance; dy <= distance; dy++) {
                if (Math.max(Math.abs(dx), Math.abs(dy)) == distance) {
                    Cell c = getCell(cx + dx, cy + dy);
                    if (c != null) result.add(c);
                }
            }
        }
        return result;
    }

    /** Tutte le celle non vuote della griglia. */
    public List<Cell> getOccupiedCells() {
        List<Cell> result = new ArrayList<>();
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if (!cells[x][y].isEmpty()) result.add(cells[x][y]);
        return result;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int getWidth()  { return width; }
    public int getHeight() { return height; }
}
