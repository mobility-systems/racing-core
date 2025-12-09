package com.theodore.racingcore.models.automobiles.drivetrain.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record DrivetrainResponseDto(Long id,
                                    EngineResponseDto engine,
                                    ElectricMotorResponseDto electricMotor,
                                    String hybridType,
                                    String transmission,
                                    Integer numberOfGears,
                                    @JsonIgnore long version) {
}
