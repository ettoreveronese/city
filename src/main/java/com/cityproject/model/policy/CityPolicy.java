package com.cityproject.model.policy;

import com.cityproject.model.CityState;

/**
 * STRATEGY PATTERN interface.
 * Each policy defines how taxes and pollution are calculated each tick.
 * Swapping a policy object changes the city's economic and environmental behavior
 * without touching any other class.
 */
public interface CityPolicy {
    String getName();
    int calculateTax(CityState city);          // extra income from tax
    double calculatePollutionModifier();        // multiplier applied to all pollution
    void applyPolicyEffects(CityState city);   // any extra side effects
}