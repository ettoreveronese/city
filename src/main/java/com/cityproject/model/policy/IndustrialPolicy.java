package com.cityproject.model.policy;

import com.cityproject.model.CityState;

/**
 * Industrial policy: higher taxes, higher pollution, lower happiness.
 */
public class IndustrialPolicy implements CityPolicy {

    @Override public String getName() { return "Industrial Policy"; }

    @Override
    public int calculateTax(CityState city) {
        // High tax: 15% of population
        return (int)(city.getPopulation() * 0.15);
    }

    @Override
    public double calculatePollutionModifier() {
        return 1.5; // pollution increased by 50%
    }

    @Override
    public void applyPolicyEffects(CityState city) {
        //city.setGlobalHappiness(city.getGlobalHappiness() - 1.0);
    }
}