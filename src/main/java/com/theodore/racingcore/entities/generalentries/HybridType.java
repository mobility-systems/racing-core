package com.theodore.racingcore.entities.generalentries;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "HYBRID_TYPE")
@DiscriminatorValue("HYBRID_TYPE")
public class HybridType extends GeneralEntry {

    public HybridType() {
    }

    public HybridType(String code) {
        this.setSpecificCode(code);
    }
}
