package com.theodore.racingcore.controllers;

import com.theodore.racingcore.models.automobiles.drivetrain.responses.EngineResponseDto;
import com.theodore.racingcore.models.general.CreateGeneralCodeRequestDto;
import com.theodore.racingcore.models.general.CreateGeneralEntryRequestDto;
import com.theodore.racingcore.services.GeneralEntriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/general-entries")
public class GeneralEntriesController {

    private final GeneralEntriesService generalEntriesService;

    public GeneralEntriesController(GeneralEntriesService generalEntriesService) {
        this.generalEntriesService = generalEntriesService;
    }

    @PostMapping("/insert/code")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<Void> insertNewGeneralCode(@RequestBody @Valid CreateGeneralCodeRequestDto request) {
        generalEntriesService.insertNewGeneralCode(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/insert/entry")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<EngineResponseDto> insertNewGeneralEntry(@RequestBody @Valid CreateGeneralEntryRequestDto request) {
        generalEntriesService.insertNewGeneralEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
