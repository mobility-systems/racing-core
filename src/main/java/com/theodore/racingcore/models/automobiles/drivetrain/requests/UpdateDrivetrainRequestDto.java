package com.theodore.racingcore.models.automobiles.drivetrain.requests;

import jakarta.validation.constraints.NotNull;

public record UpdateDrivetrainRequestDto(Long engine,
                                         Long electricMotor,
                                         @NotNull String transmission,
                                         Integer numberOfGears) {
}
