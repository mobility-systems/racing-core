package com.theodore.racingcore.models.automobiles.drivetrain.requests;

import jakarta.validation.constraints.NotNull;

public record CreateNewDrivetrainRequestDto(Long engine,
                                            Long electricMotor,
                                            @NotNull String transmission,
                                            Integer numberOfGears) {
}
