package com.cityproject.model.policy;

import com.cityproject.model.CityState;

/**
 * Green policy: lower taxes, lower pollution, higher happiness.
 */
public class GreenPolicy implements CityPolicy {

    @Override public String getName() { return "Green Policy"; }

    @Override
    public int calculateTax(CityState city) {
        // Low tax: 5% of population
        return (int)(city.getPopulation() * 0.05);
    }

    @Override
    public double calculatePollutionModifier() {
        return 0.5; // pollution is halved under this policy
    }

    @Override
    public void applyPolicyEffects(CityState city) {
        city.setGlobalHappiness(city.getGlobalHappiness() + 2.0);
    }
}