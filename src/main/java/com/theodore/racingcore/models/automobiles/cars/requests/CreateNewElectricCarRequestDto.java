package com.theodore.racingcore.models.automobiles.cars.requests;

import com.theodore.racingcore.entities.enums.CarType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateNewElectricCarRequestDto(@NotNull Long carModel,
                                             @Valid @NotNull TechnicalDetailsDto technicalDetails,
                                             @NotNull @Min(0) Integer electricRange,
                                             @NotNull Long drivetrain) implements CreateNewCarRequest {
    @Override
    public CarType type() {
        return CarType.BEV;
    }

}
