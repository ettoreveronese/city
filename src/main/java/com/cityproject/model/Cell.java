package com.cityproject.model;

/**
 * Represents a single cell of the MxN grid.
 * Each cell tracks local effects and holds at most one structure.
 */
public class Cell {

    private final int x;
    private final int y;
    private int pollution;          // current pollution level in this cell
    private boolean fireProtection; // true if covered by a FireStation
    private Infrastructure structure; // null if empty

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.pollution = 0;
        this.fireProtection = false;
        this.structure = null;
    }

    public boolean isEmpty()    { return structure == null; }

    public int getX()           { return x; }
    public int getY()           { return y; }
    
    public int getPollution()   { return pollution; }
    public void setPollution(int pollution) { this.pollution = Math.max(0, pollution); }
    
    public boolean hasFireProtection()  { return fireProtection; }
    public void setFireProtection(boolean fp) { this.fireProtection = fp; }
    
    public Infrastructure getStructure()    { return structure; }
    public void setStructure(Infrastructure s) { this.structure = s; }
}