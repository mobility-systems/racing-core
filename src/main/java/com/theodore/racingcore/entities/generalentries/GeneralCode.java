package com.theodore.racingcore.entities.generalentries;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "general_codes")
public class GeneralCode {

    @Id
    @Column(name = "general_code", nullable = false, length = 16)
    private String generalCodeId;

    @Column(name = "description")
    private String description;

    public String getGeneralCodeId() {
        return generalCodeId;
    }

    public void setGeneralCodeId(String generalCodeId) {
        this.generalCodeId = generalCodeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}