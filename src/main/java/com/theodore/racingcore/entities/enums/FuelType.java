package com.theodore.racingcore.entities.enums;

public enum FuelType {

    DIESEL("Diesel"),

    GASOLINE("Gasoline"),

    CNG("Compressed Natural Gas"),

    LNG("Liquefied Natural Gas"),

    LPG("Liquefied Petroleum Gas"),

    ETHANOL("Ethanol"),

    METHANOL("Methanol"),

    BIODIESEL("Biodiesel"),

    HYDROGEN("Hydrogen"),

    SYNTHETIC_FUELS("Synthetic Fuels");

    private final String value;

    private FuelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
