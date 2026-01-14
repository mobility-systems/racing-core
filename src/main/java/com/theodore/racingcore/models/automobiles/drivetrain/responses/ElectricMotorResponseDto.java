package com.theodore.racingcore.models.automobiles.drivetrain.responses;

public record ElectricMotorResponseDto(Long id,
                                       Integer batteryCapacity,
                                       String batteryType) {
}
