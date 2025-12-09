package com.theodore.racingcore.models.racing.respones;

import com.theodore.infrastructure.common.enums.Country;

public record TrackResponseDto(Long id,
                               String trackName,
                               Country country,
                               String city,
                               Integer trackLength,
                               Boolean racingApproved,
                               long version) {
}
