package com.theodore.racingcore.models.racing.respones;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theodore.infrastructure.common.entities.enums.Country;

public record TrackResponseDto(Long id,
                               String trackName,
                               Country country,
                               String city,
                               Integer trackLength,
                               Boolean racingApproved,
                               @JsonIgnore long version) {
}
