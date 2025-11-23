package com.theodore.racingcore.models.automobiles.drivetrain.responses;

import com.theodore.racingcore.entities.enums.EngineType;
import com.theodore.racingcore.entities.enums.FuelType;
import com.theodore.racingcore.models.automobiles.manufacturers.responses.BasicManufacturerInfoResponseDto;

public record EngineResponseDto(Long id,
                                BasicManufacturerInfoResponseDto manufacturer,
                                Integer horsepower,
                                Integer torque,
                                String motorCode,
                                Integer displacement,
                                EngineType engineType,
                                FuelType fuelType,
                                long version) {
}
