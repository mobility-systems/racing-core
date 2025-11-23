package com.theodore.racingcore.models.automobiles.cars.requests;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.theodore.racingcore.entities.enums.CarType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateNewIceCarRequestDto.class, name = "ICE"),
        @JsonSubTypes.Type(value = CreateNewHybridCarRequestDto.class, name = "HYBRID"),
        @JsonSubTypes.Type(value = CreateNewElectricCarRequestDto.class, name = "BEV")
})
public sealed interface CreateNewCarRequest
        permits CreateNewIceCarRequestDto, CreateNewHybridCarRequestDto, CreateNewElectricCarRequestDto {

    CarType type();

    Long carModel();

    TechnicalDetailsDto technicalDetails();

    Long drivetrain();

}
