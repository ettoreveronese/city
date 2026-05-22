package city.model;

import city.entity.Entity;
import java.util.ArrayList;
import java.util.List;

// current implementation: 1 entity per cell, to review if multi cell entities are implemented

// represents the urban grid
// manages the positioning of entities and search of neighbors  
public class CityGrid {
    private final int width;
    private final int height;
    private final Cell[][] cells;

    // initialize an empty grid
    public CityGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];

        // initializes the matrix filling it with empty cell objects
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // checks if the passed set of coordinates are inside the grid
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    // returns the entity at given set of coordinates, null if cell is empty or out of bounds
    public Entity getEntity(int x, int y) {
        if (!isValidCoordinate(x, y)) {
            return null;
        }
        return cells[x][y].getEntity();
    }

    // position an entity at a given set of coordinates
    public void setEntity(int x, int y, Entity entity) {
        if (!isValidCoordinate(x, y)) {
            throw new IndexOutOfBoundsException("Invalid coordinates, not inside the grid: (" + x + ", " + y + ")");
        }
        cells[x][y].setEntity(entity);
    }

    // returns as a list entities inside neighboring cells
    public List<Cell> getNeighbors(int x, int y) {
        List<Cell> neighbors = new ArrayList<>();

        if (!isValidCoordinate(x, y)) {
            return neighbors;
        }

        // iterates through neighboring cells (3x3)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // skip the central cell
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int neighborX = x + dx;
                int neighborY = y + dy;
                
                // add to list if inside grid
                if (isValidCoordinate(neighborX, neighborY)) {
                    neighbors.add(cells[neighborX][neighborY]);
                }
            }
        }
        return neighbors;
    }
}
