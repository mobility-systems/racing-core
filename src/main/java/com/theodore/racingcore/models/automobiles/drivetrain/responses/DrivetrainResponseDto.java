package com.theodore.racingcore.models.automobiles.drivetrain.responses;

public record DrivetrainResponseDto(Long id,
                                    EngineResponseDto engine,
                                    ElectricMotorResponseDto electricMotor,
                                    String hybridType,
                                    String transmission,
                                    Integer numberOfGears,
                                    long version) {
}
