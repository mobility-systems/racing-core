package com.theodore.racingcore.models.racing.respones;

import com.theodore.racingcore.models.automobiles.cars.responses.CarModelResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DriversLapsPreviewResponseDto(Long id,
                                            TrackResponseDto track,
                                            CarModelResponseDto car,
                                            LocalDateTime lapDate,
                                            BigDecimal lapTime,
                                            Boolean lapApproved) {
}
