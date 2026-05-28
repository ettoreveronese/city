package com.cityproject.model.aspects;

/**
 * Aspect for buildings that house people.
 * Implemented by: Residence
 */
public interface HasHousing {
    int getCapacity();   // max number of residents
    int getRent();       // income per tick from rent
    double getLocalHappiness(); // happiness contribution to residents
    double getLocalHealth();    // health contribution to residents
    void setLocalHappiness(double h);
    void setLocalHealth(double h);
}