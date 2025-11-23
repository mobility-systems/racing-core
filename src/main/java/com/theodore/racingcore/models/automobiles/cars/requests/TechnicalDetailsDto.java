package com.theodore.racingcore.models.automobiles.cars.requests;

import com.theodore.racingcore.entities.enums.DrivetrainPlace;
import com.theodore.racingcore.entities.enums.WheelsDriven;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TechnicalDetailsDto(@Digits(integer = 2, fraction = 2) BigDecimal zeroToOneHundred,
                                  @Min(1) Integer topSpeed,
                                  @NotNull @Min(100) Integer weight,
                                  @NotNull WheelsDriven wheelsDriven,
                                  @NotNull DrivetrainPlace drivetrainPlace,
                                  @NotNull @Min(1) Integer totalHorsePower) {
}
