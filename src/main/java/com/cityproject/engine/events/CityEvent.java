package com.cityproject.engine.events;

import com.cityproject.model.CityState;

/**
 * Classe base per tutti gli eventi casuali.
 * Ogni evento sa applicarsi al CityState.
 */
public abstract class CityEvent {
    public abstract void apply(CityState city);
}
