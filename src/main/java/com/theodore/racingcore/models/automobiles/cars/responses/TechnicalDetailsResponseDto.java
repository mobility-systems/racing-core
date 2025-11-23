package com.theodore.racingcore.models.automobiles.cars.responses;

import com.theodore.racingcore.entities.enums.DrivetrainPlace;
import com.theodore.racingcore.entities.enums.WheelsDriven;

import java.math.BigDecimal;

public record TechnicalDetailsResponseDto(BigDecimal zeroToOneHundred,
                                          Integer topSpeed,
                                          BigDecimal cityConsumption,
                                          BigDecimal highwayConsumption,
                                          Integer electricRange,
                                          Integer weight,
                                          WheelsDriven wheelsDriven,
                                          DrivetrainPlace drivetrainPlace,
                                          Integer totalHorsePower) {
}
