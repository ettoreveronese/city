package com.cityproject.model.components;

public class MaintenanceComponent implements Component {
    private int maintenanceCost;

    public MaintenanceComponent(int maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(int maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }
}
