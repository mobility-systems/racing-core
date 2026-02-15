package com.theodore.racingcore.models.racing.respones;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theodore.racingcore.models.automobiles.cars.responses.CarModelResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LapPreviewResponseDto(Long id,
                                    String driverAlias,
                                    String trackName,
                                    CarModelResponseDto car,
                                    LocalDateTime lapDate,
                                    BigDecimal lapTime,
                                    Boolean specialTires,
                                    Boolean lapApproved,
                                    Integer highestRecordedSpeed,
                                    @JsonIgnore long version) {
}
