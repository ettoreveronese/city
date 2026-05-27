package com.cityproject.model.aspects;

/**
 * Aspect for buildings that provide fire protection to nearby cells.
 * Implemented by: FireStation
 */
public interface HasFireCoverage {
    int getFireRadius(); // how many cells are protected from fire events
}