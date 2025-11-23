package com.theodore.racingcore.models.automobiles.cars.requests;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateIceCarRequestDto.class, name = "ICE"),
        @JsonSubTypes.Type(value = UpdateHybridCarRequestDto.class, name = "HYBRID"),
        @JsonSubTypes.Type(value = UpdateElectricCarRequestDto.class, name = "BEV")
})
public sealed interface UpdateCarRequest permits UpdateIceCarRequestDto, UpdateHybridCarRequestDto, UpdateElectricCarRequestDto {

    TechnicalDetailsDto technicalDetails();

    Long drivetrain();

}
