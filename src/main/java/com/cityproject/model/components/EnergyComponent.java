package com.cityproject.model.components;

public class EnergyComponent implements Component {
    private int consumption;
    private int production;

    public EnergyComponent(int consumption, int production) {
        this.consumption = consumption;
        this.production = production;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public int getProduction() {
        return production;
    }

    public void setProduction(int production) {
        this.production = production;
    }
}
