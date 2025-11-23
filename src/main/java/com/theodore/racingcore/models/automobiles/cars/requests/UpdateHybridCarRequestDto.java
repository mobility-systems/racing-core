package com.theodore.racingcore.models.automobiles.cars.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateHybridCarRequestDto(@Valid @NotNull TechnicalDetailsDto technicalDetails,
                                        @Valid @NotNull HybridDetailsDto hybridDetails,
                                        @NotNull Long drivetrain)  implements UpdateCarRequest {

}
