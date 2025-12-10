package com.theodore.racingcore.services;

import com.theodore.infrastructure.common.exceptions.AlreadyExistsException;
import com.theodore.racingcore.entities.racing.Track;
import com.theodore.racingcore.mappers.TrackMapper;
import com.theodore.racingcore.models.racing.requests.CreateNewTrackRequestDto;
import com.theodore.racingcore.models.racing.respones.TrackResponseDto;
import com.theodore.racingcore.repositories.TrackRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RacingServiceImpl implements RacingService {

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    public RacingServiceImpl(TrackRepository trackRepository, TrackMapper trackMapper) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
    }

    @Override
    public TrackResponseDto createNewTrack(CreateNewTrackRequestDto request) {
        if (trackRepository.existsByTrackNameIgnoreCaseAndCountry(request.trackName(), request.country())) {
            throw new AlreadyExistsException(request.trackName() + " already exists in " + request.country());
        }
        Track newTrack = trackMapper.toEntity(request);
        Instant now = Instant.now();
        newTrack.setDateCreated(now);
        newTrack.setDateUpdated(now);
        Track savedTrack = trackRepository.save(newTrack);
        return trackMapper.toResponse(savedTrack);
    }

    @Override
    public TrackResponseDto getTrack(Long id) {
        var track = trackRepository.findById(id).orElseThrow();
        return trackMapper.toResponse(track);
    }
}
