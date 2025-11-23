package com.theodore.racingcore.models.automobiles.cars.requests;

import com.theodore.racingcore.entities.enums.CarType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateNewIceCarRequestDto(@NotNull Long carModel,
                                        @Valid @NotNull TechnicalDetailsDto technicalDetails,
                                        @Valid @NotNull IceTechnicalDetailsDto iceTechnicalDetails,
                                        @NotNull Long drivetrain) implements CreateNewCarRequest {
    @Override
    public CarType type() {
        return CarType.ICE;
    }

}
