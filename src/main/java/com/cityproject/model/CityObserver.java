package com.cityproject.model;

/**
 * OBSERVER PATTERN interface.
 * Any class that wants to be notified when the city state changes
 * must implement this interface.
 * Examples: JavaFX Dashboard, Logger, PolicyChecker.
 */
public interface CityObserver {
    void onCityUpdated(CityState city);
}