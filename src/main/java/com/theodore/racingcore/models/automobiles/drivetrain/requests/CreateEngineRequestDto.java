package com.theodore.racingcore.models.automobiles.drivetrain.requests;

import com.theodore.racingcore.entities.enums.EngineType;
import com.theodore.racingcore.entities.enums.FuelType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEngineRequestDto(@NotNull Long manufacturerId,
                                     @NotNull @Min(1) Integer horsepower,
                                     @NotNull @Min(1) Integer torque,
                                     @Size(max = 100) String motorCode,
                                     @NotNull @Min(1) Integer displacement,
                                     @NotNull EngineType engineType,
                                     @NotNull FuelType fuelType) {
}
