package com.theodore.racingcore.controllers;

import com.theodore.racingcore.models.racing.requests.CreateNewTrackRequestDto;
import com.theodore.racingcore.models.racing.respones.TrackResponseDto;
import com.theodore.racingcore.services.RacingService;
import com.theodore.racingcore.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    //@PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<TrackResponseDto> insertNewTrack(@RequestBody @Valid CreateNewTrackRequestDto request) {
        var response = racingService.createNewTrack(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/track/by-id/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/track/by-id/{id}")
    public ResponseEntity<TrackResponseDto> getTrack(@PathVariable Long id) {
        var response = racingService.getTrack(id);
        return ResponseEntity.ok()
                .eTag(Utils.toEtag(response.version()))
                .body(response);
    }


}
