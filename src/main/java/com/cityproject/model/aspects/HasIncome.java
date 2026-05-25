package com.cityproject.model.aspects;

/**
 * Aspect for buildings that generate money each tick.
 * Implemented by: ProductionBuilding
 */
public interface HasIncome {
    int getIncome(); // money added to budget each tick
}