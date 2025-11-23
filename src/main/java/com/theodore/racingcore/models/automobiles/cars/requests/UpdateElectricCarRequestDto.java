package com.theodore.racingcore.models.automobiles.cars.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateElectricCarRequestDto(@Valid @NotNull TechnicalDetailsDto technicalDetails,
                                          @NotNull @Min(0) Integer electricRange,
                                          @NotNull Long drivetrain) implements UpdateCarRequest {

}
