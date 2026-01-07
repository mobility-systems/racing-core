package com.theodore.racingcore.models.racing.requests;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateNewLapRequestDto(String driverId,
                                     Long trackId,
                                     Long carId,
                                     LocalDateTime lapDate,
                                     BigDecimal lapTime,
                                     Boolean specialTires,
                                     Integer highestRecordedSpeed) {
}
