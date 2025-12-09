package com.theodore.racingcore.services;

import com.theodore.racingcore.models.racing.requests.CreateNewTrackRequestDto;
import com.theodore.racingcore.models.racing.respones.TrackResponseDto;

public interface RacingService {

    TrackResponseDto createNewTrack(CreateNewTrackRequestDto request);

    TrackResponseDto getTrack(Long id);

}
