package com.theodore.racingcore.controllers;

import com.theodore.racingcore.models.automobiles.drivetrain.requests.*;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.DrivetrainResponseDto;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.ElectricMotorResponseDto;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.EngineResponseDto;
import com.theodore.racingcore.services.DrivetrainService;
import com.theodore.racingcore.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/drivetrain")
public class DrivetrainController {

    private final DrivetrainService drivetrainService;

    public DrivetrainController(DrivetrainService drivetrainService) {
        this.drivetrainService = drivetrainService;
    }

    @PostMapping("/create")
    //@PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<DrivetrainResponseDto> createNewDrivetrain(@RequestBody @Valid CreateNewDrivetrainRequestDto request) {
        var response = drivetrainService.createNewDrivetrain(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/by-id/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/update/{id}")
    //@PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<DrivetrainResponseDto> updateDrivetrainEndpoint(@PathVariable Long id,
                                                                          @RequestBody @Valid UpdateDrivetrainRequestDto request,
                                                                          @RequestHeader(value = "If-Match") String ifMatch) {
        var response = drivetrainService.updateDrivetrain(id, request, ifMatch);
        return ResponseEntity.ok()
                .eTag(Utils.toEtag(response.version()))
                .body(response);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<DrivetrainResponseDto> fetchDrivetrainById(@PathVariable Long id) {
        return ResponseEntity.ok().body(drivetrainService.getDrivetrainResponseById(id));
    }

    @PostMapping("/engine/create")
    //@PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<EngineResponseDto> insertNewEngine(@RequestBody @Valid CreateEngineRequestDto request) {
        var response = drivetrainService.insertNewEngine(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/engine/by-id/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/engine/update/{id}")
    //@PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<EngineResponseDto> updateEngine(@PathVariable Long id,
                                                          @RequestBody @Valid UpdateEngineRequestDto request,
                                                          @RequestHeader(value = "If-Match") String ifMatch) {
        var response = drivetrainService.updateEngine(id, request, ifMatch);
        return ResponseEntity.ok()
                .eTag(Utils.toEtag(response.version()))
                .body(response);
    }

    @GetMapping("/engine/by-id/{id}")
    public ResponseEntity<EngineResponseDto> fetchEngineById(@PathVariable Long id) {
        return ResponseEntity.ok().body(drivetrainService.getEngineResponseById(id));
    }

    @PostMapping("/electric-motor/create")
    //@PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<ElectricMotorResponseDto> insertNewElectricMotor(@RequestBody @Valid CreateElectricMotorRequestDto request) {
        var response = drivetrainService.insertNewElectricMotor(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/by-id/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    //todo electric motor update

}
