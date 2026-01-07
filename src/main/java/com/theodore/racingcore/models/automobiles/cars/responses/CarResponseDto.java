package com.theodore.racingcore.models.automobiles.cars.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.DrivetrainResponseDto;

public record CarResponseDto(Long id,
                             CarModelResponseDto carModel,
                             DrivetrainResponseDto drivetrain,
                             TechnicalDetailsResponseDto technicalDetails,
                             @JsonIgnore long version) {
}
