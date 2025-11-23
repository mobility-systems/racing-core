package com.theodore.racingcore.entities.generalentries;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "TRANSMISSION_TYPE")
@DiscriminatorValue("TRANSMISSION_TYPE")
public class TransmissionType extends GeneralEntry {

    public TransmissionType() {
    }

    public TransmissionType(String code) {
        this.setSpecificCode(code);
    }
}
