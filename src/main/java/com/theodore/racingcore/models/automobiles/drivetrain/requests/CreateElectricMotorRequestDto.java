package com.theodore.racingcore.models.automobiles.drivetrain.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateElectricMotorRequestDto(@NotNull Long manufacturerId,
                                            @NotNull @Min(1) Integer horsepower,
                                            @NotNull @Min(1) Integer torque,
                                            @Size(max = 100) String motorCode,
                                            @Min(1) Integer batteryCapacity,
                                            @NotBlank @Size(max = 16) String batteryTypeCode) {
}
