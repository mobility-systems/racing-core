package com.theodore.racingcore.services;

import com.theodore.infrastructure.common.entities.enums.RoleType;
import com.theodore.infrastructure.common.exceptions.AlreadyExistsException;
import com.theodore.infrastructure.common.exceptions.NotFoundException;
import com.theodore.infrastructure.common.saga.SagaOrchestrator;
import com.theodore.queue.common.emails.EmailDto;
import com.theodore.queue.common.services.MessagingService;
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
import com.theodore.racingcore.services.saga.SagaCompensationActionService;
import com.theodore.racingcore.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class RacingServiceImpl implements RacingService {

    private static final String SEND_AUTH_USER_ACCOUNT_CHANGES_STEP = "send-auth-user-account-changes";
    private static final String SAVE_DRIVER_STEP = "save-driver";
    private static final String SEND_EMAIL_STEP = "send-to-email-service";

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;
    private final AccountManagementRestClient accountManagementRestClient;
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final AuthServerGrpcClient authServerGrpcClient;
    private final MessagingService messagingService;
    private final SagaCompensationActionService sagaCompensationActionService;

    public RacingServiceImpl(TrackRepository trackRepository,
                             TrackMapper trackMapper,
                             AccountManagementRestClient accountManagementRestClient,
                             DriverRepository driverRepository,
                             DriverMapper driverMapper,
                             AuthServerGrpcClient authServerGrpcClient,
                             MessagingService messagingService,
                             SagaCompensationActionService sagaCompensationActionService) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
        this.accountManagementRestClient = accountManagementRestClient;
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
        this.authServerGrpcClient = authServerGrpcClient;
        this.messagingService = messagingService;
        this.sagaCompensationActionService = sagaCompensationActionService;
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
    public String createNewDriver(String alias) {

        var userId = Utils.getLoggedInUserId();

        if (driverRepository.existsByIdOrAliasIgnoreCase(userId, alias)) {
            throw new AlreadyExistsException("Driver with alias %s already exists".formatted(alias));
        }

        Driver driver = new Driver();
        driver.setId(userId);
        driver.setAlias(alias);

        var sagaOrchestrator = new SagaOrchestrator();

        sagaOrchestrator
                .step(SAVE_DRIVER_STEP,
                        () -> driverRepository.save(driver),
                        () -> driverRepository.delete(driver)
                )
                .step(SEND_AUTH_USER_ACCOUNT_CHANGES_STEP,
                        () -> authServerGrpcClient.addUserRoleInAuthServer(userId, RoleType.DRIVER),
                        () -> {
                            String logMsg = "New Driver Registration";
                            sagaCompensationActionService.authServerRolesRollback(userId, Set.of(RoleType.DRIVER), logMsg);
                        }
                )
                .step(SEND_EMAIL_STEP,
                        () -> sendEmailToNewDriver(userId),
                        () -> {
                        }
                );

        sagaOrchestrator.run();

        return userId;
    }

    private void sendEmailToNewDriver(String userId) {
        var userEmail = accountManagementRestClient.fetchUserEmail(userId);
        messagingService.sendToEmailService(new EmailDto(List.of(userEmail), "Successful driver sign up", "Congratulations on becoming a driver for our platform!!"));
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
