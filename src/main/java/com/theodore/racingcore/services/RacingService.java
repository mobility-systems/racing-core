package com.theodore.racingcore.services;

import com.theodore.racingcore.models.racing.requests.CreateNewTrackRequestDto;
import com.theodore.racingcore.models.racing.respones.DriverResponseDto;
import com.theodore.racingcore.models.racing.respones.TrackResponseDto;

public interface RacingService {

    TrackResponseDto createNewTrack(CreateNewTrackRequestDto request);

    TrackResponseDto updateTrack(Long id, CreateNewTrackRequestDto track, String ifMatch);

    TrackResponseDto getTrack(Long id);

    String createNewDriver(String alias);

    DriverResponseDto getDriver(String id);

}
