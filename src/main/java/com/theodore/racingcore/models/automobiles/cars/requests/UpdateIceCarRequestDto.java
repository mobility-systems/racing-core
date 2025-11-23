package com.theodore.racingcore.models.automobiles.cars.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateIceCarRequestDto(@Valid @NotNull TechnicalDetailsDto technicalDetails,
                                     @Valid @NotNull IceTechnicalDetailsDto iceTechnicalDetails,
                                     @NotNull Long drivetrain) implements UpdateCarRequest {

}
