package com.theodore.racingcore.services;

import com.theodore.infrastructure.common.exceptions.AlreadyExistsException;
import com.theodore.racingcore.entities.racing.Track;
import com.theodore.racingcore.models.racing.requests.CreateNewTrackRequestDto;
import com.theodore.racingcore.models.racing.respones.TrackResponseDto;
import org.springframework.stereotype.Service;

@Service
public class RacingServiceImpl implements RacingService {

    private final TrackService trackService;

    public RacingServiceImpl(TrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public TrackResponseDto createNewTrack(CreateNewTrackRequestDto request) {
        if (trackService.existsByNameAndCountry(request.trackName(), request.country())) {
            throw new AlreadyExistsException(request.trackName() + " already exists in " + request.country());
        }
        Track newTrack = trackService.mapRequestToEntity(request);
        Track savedTrack = trackService.saveNewTrack(newTrack);
        return trackService.mapEntityToResponse(savedTrack);
    }

    @Override
    public TrackResponseDto getTrack(Long id) {
        var track = trackService.getTrackById(id);
        return trackService.mapEntityToResponse(track);
    }
}
