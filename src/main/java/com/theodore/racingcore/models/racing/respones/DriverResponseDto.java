package com.theodore.racingcore.models.racing.respones;

import java.util.List;

public record DriverResponseDto(String alias, Integer trophies, List<DriversLapsPreviewResponseDto> laps) {
}
