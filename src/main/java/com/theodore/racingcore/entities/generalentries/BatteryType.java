package com.theodore.racingcore.entities.generalentries;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "BATTERY_TYPE")
@DiscriminatorValue("BATTERY_TYPE")
public class BatteryType extends GeneralEntry {

    public BatteryType() {
    }

    public BatteryType(String code) {
        this.setSpecificCode(code);
    }
}
