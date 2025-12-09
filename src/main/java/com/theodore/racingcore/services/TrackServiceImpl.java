package com.theodore.racingcore.services;

import com.theodore.infrastructure.common.enums.Country;
import com.theodore.infrastructure.common.exceptions.NotFoundException;
import com.theodore.racingcore.entities.racing.Track;
import com.theodore.racingcore.mappers.TrackMapper;
import com.theodore.racingcore.models.racing.requests.CreateNewTrackRequestDto;
import com.theodore.racingcore.models.racing.respones.TrackResponseDto;
import com.theodore.racingcore.repositories.TrackRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    public TrackServiceImpl(TrackRepository trackRepository, TrackMapper trackMapper) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
    }

    @Override
    public boolean existsByNameAndCountry(String trackName, Country country) {
        return trackRepository.existsByTrackNameIgnoreCaseAndCountry(trackName, country);
    }

    @Override
    @Transactional
    public Track saveNewTrack(Track track) {
        track.setDateCreated(Instant.now());
        track.setDateUpdated(Instant.now());
        return trackRepository.save(track);
    }

    @Override
    public Track mapRequestToEntity(CreateNewTrackRequestDto newTrackRequestDto) {
        return trackMapper.toEntity(newTrackRequestDto);
    }

    @Override
    public TrackResponseDto mapEntityToResponse(Track track) {
        return trackMapper.toResponse(track);
    }

    @Override
    public Track getTrackById(Long id) {
        return trackRepository.findById(id).orElseThrow(() -> new NotFoundException("Track Not found"));
    }
}
