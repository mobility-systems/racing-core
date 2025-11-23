package com.theodore.racingcore.models.automobiles.drivetrain.responses;

import com.theodore.racingcore.entities.generalentries.BatteryType;

public record ElectricMotorResponseDto(Long id,
                                       Integer batteryCapacity,
                                       BatteryType batteryType) {
}
