package com.theodore.racingcore.services;

import com.theodore.infrastructure.common.enums.Country;
import com.theodore.racingcore.entities.racing.Track;
import com.theodore.racingcore.models.racing.requests.CreateNewTrackRequestDto;
import com.theodore.racingcore.models.racing.respones.TrackResponseDto;

public interface TrackService {

    boolean existsByNameAndCountry(String trackName, Country country);

    Track saveNewTrack(Track track);

    Track mapRequestToEntity(CreateNewTrackRequestDto newTrackRequestDto);

    TrackResponseDto mapEntityToResponse(Track track);

    Track getTrackById(Long id);

}
