package com.cityproject.model.components;

public class IncomeComponent implements Component {
    private int baseIncome;

    public IncomeComponent(int baseIncome) {
        this.baseIncome = baseIncome;
    }

    public int getBaseIncome() {
        return baseIncome;
    }

    public void setBaseIncome(int baseIncome) {
        this.baseIncome = baseIncome;
    }
}
