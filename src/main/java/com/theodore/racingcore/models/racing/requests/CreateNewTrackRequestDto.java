package com.theodore.racingcore.models.racing.requests;

import com.theodore.infrastructure.common.enums.Country;

public record CreateNewTrackRequestDto(String trackName,
                                       Country country,
                                       String city,
                                       Integer trackLength,
                                       Boolean racingApproved) {
}
