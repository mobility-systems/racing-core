package com.theodore.racingcore.controllers;

import com.theodore.racingcore.models.racing.requests.CreateNewDriverRequestDto;
import com.theodore.racingcore.models.racing.requests.CreateNewLapRequestDto;
import com.theodore.racingcore.models.racing.requests.CreateNewTrackRequestDto;
import com.theodore.racingcore.models.racing.respones.DriverResponseDto;
import com.theodore.racingcore.models.racing.respones.LapPreviewResponseDto;
import com.theodore.racingcore.models.racing.respones.TrackResponseDto;
import com.theodore.racingcore.services.RacingService;
import com.theodore.racingcore.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/racing")
public class RacingController {

    private final RacingService racingService;

    public RacingController(RacingService racingService) {
        this.racingService = racingService;
    }

    @PostMapping("/track/create")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<TrackResponseDto> insertNewTrack(@RequestBody @Valid CreateNewTrackRequestDto request) {
        var response = racingService.createNewTrack(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/racing/track/by-id/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/track/update/{id}")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<TrackResponseDto> updateTrack(@PathVariable Long id,
                                                               @RequestBody @Valid CreateNewTrackRequestDto request,
                                                               @RequestHeader(value = "If-Match") String ifMatch) {
        var response = racingService.updateTrack(id, request, ifMatch);

        return ResponseEntity.ok().eTag(Utils.toEtag(response.version())).body(response);
    }

    @GetMapping("/track/by-id/{id}")
    public ResponseEntity<TrackResponseDto> getTrack(@PathVariable Long id) {
        var response = racingService.getTrack(id);
        return ResponseEntity.ok().eTag(Utils.toEtag(response.version())).body(response);
    }

    @PostMapping("/driver/create")
    @PreAuthorize("hasRole('SIMPLE_USER')")
    public ResponseEntity<Void> insertNewDriver(@RequestBody @Valid CreateNewDriverRequestDto request) {
        String userId = racingService.createNewDriver(request.alias());
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/racing/driver/by-id/{id}")
                .buildAndExpand(userId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/driver/by-id/{id}")
    public ResponseEntity<DriverResponseDto> getDriver(@PathVariable String id) {
        var response = racingService.getDriver(id);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/lap/create")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<LapPreviewResponseDto> createNewLap(@RequestBody @Valid CreateNewLapRequestDto request) {
        LapPreviewResponseDto response = racingService.createNewLap(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/racing/lap/by-id/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

}
