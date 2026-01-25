package com.theodore.racingcore.services;

import com.theodore.infrastructure.common.entities.modeltypes.RoleType;
import com.theodore.infrastructure.common.exceptions.AlreadyExistsException;
import com.theodore.infrastructure.common.exceptions.InvalidTokenException;
import com.theodore.infrastructure.common.exceptions.NotFoundException;
import com.theodore.racingcore.entities.racing.Driver;
import com.theodore.racingcore.entities.racing.Track;
import com.theodore.racingcore.exceptions.InvalidETagException;
import com.theodore.racingcore.mappers.DriverMapper;
import com.theodore.racingcore.mappers.TrackMapper;
import com.theodore.racingcore.models.racing.requests.CreateNewLapRequestDto;
import com.theodore.racingcore.models.racing.requests.CreateNewTrackRequestDto;
import com.theodore.racingcore.models.racing.respones.DriverResponseDto;
import com.theodore.racingcore.models.racing.respones.LapPreviewResponseDto;
import com.theodore.racingcore.models.racing.respones.TrackResponseDto;
import com.theodore.racingcore.repositories.DriverRepository;
import com.theodore.racingcore.repositories.TrackRepository;
import com.theodore.racingcore.services.clients.AccountManagementRestClient;
import com.theodore.racingcore.services.clients.AuthServerGrpcClient;
import com.theodore.racingcore.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class RacingServiceImpl implements RacingService {

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;
    private final AccountManagementRestClient accountManagementRestClient;
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final AuthServerGrpcClient authServerGrpcClient;

    public RacingServiceImpl(TrackRepository trackRepository,
                             TrackMapper trackMapper,
                             AccountManagementRestClient accountManagementRestClient,
                             DriverRepository driverRepository,
                             DriverMapper driverMapper,
                             AuthServerGrpcClient authServerGrpcClient) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
        this.accountManagementRestClient = accountManagementRestClient;
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
        this.authServerGrpcClient = authServerGrpcClient;
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
    public TrackResponseDto updateTrack(Long id, CreateNewTrackRequestDto updateTrackRequest, String ifMatch) {
        var track = getTrackById(id);

        if (track.getVersion() != Utils.parseIfMatch(ifMatch)) {
            throw new InvalidETagException();
        }
        trackMapper.updateEntity(track, updateTrackRequest);

        Track updatedTrack = trackRepository.save(track);

        return trackMapper.toResponse(updatedTrack);
    }

    @Override
    public TrackResponseDto getTrack(Long id) {
        var track = getTrackById(id);
        return trackMapper.toResponse(track);
    }

    @Override
    @Transactional
    public String createNewDriver(String alias) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken auth) || !auth.isAuthenticated() || auth.getToken() == null) {
            throw new InvalidTokenException("Invalid or empty token");
        }
        var username = auth.getToken().getClaimAsString("username");

        var userId = accountManagementRestClient.fetchUserId(username);

        if (driverRepository.existsByIdOrAliasIgnoreCase(userId, alias)) {
            throw new AlreadyExistsException("Driver with alias %s already exists".formatted(alias));
        }

        Driver driver = new Driver();
        driver.setId(userId);
        driver.setAlias(alias);

        driverRepository.save(driver);

        authServerGrpcClient.addUserRoleInAuthServer(userId, RoleType.DRIVER);

        return userId;
    }

    @Override
    public DriverResponseDto getDriver(String id) {
        var driver = driverRepository.findById(id).orElseThrow(() -> new NotFoundException("Driver not found"));
        return driverMapper.toResponse(driver);
    }

    @Override
    public LapPreviewResponseDto createNewLap(CreateNewLapRequestDto request) {
        return null;//todo
    }

    private Track getTrackById(Long id) {
        return trackRepository.findById(id).orElseThrow(() -> new NotFoundException("Track not found"));
    }
}
