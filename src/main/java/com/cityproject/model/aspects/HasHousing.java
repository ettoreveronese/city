package com.cityproject.model.aspects;

/**
 * Aspect for buildings that house people.
 * Implemented by: Residence
 */
public interface HasHousing {
    int getCapacity();   // max number of residents
    int getRent();       // income per tick from rent
}