package com.theodore.racingcore.services;

import com.theodore.racingcore.entities.cars.Drivetrain;
import com.theodore.racingcore.entities.generalentries.TransmissionType;
import com.theodore.racingcore.models.automobiles.drivetrain.requests.*;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.DrivetrainResponseDto;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.ElectricMotorResponseDto;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.EngineResponseDto;

public interface DrivetrainService {

    DrivetrainResponseDto createNewDrivetrain(CreateNewDrivetrainRequestDto request);

    DrivetrainResponseDto updateDrivetrain(Long id, UpdateDrivetrainRequestDto request, String ifMatch);

    EngineResponseDto insertNewEngine(CreateEngineRequestDto request);

    EngineResponseDto updateEngine(Long id, UpdateEngineRequestDto request, String ifMatch);

    ElectricMotorResponseDto insertNewElectricMotor(CreateElectricMotorRequestDto request);

    Drivetrain saveDrivetrain(Drivetrain drivetrain);

    EngineResponseDto getEngineResponseById(Long id);

    DrivetrainResponseDto getDrivetrainResponseDto(Drivetrain drivetrain);

    TransmissionType getTransmissionType(String transmissionCode);

    Drivetrain getDrivetrainEntityById(Long id);

    DrivetrainResponseDto getDrivetrainResponseById(Long id);

}
