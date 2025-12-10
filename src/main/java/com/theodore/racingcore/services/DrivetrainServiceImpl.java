package com.theodore.racingcore.services;

import com.theodore.infrastructure.common.exceptions.NotFoundException;
import com.theodore.racingcore.entities.cars.Drivetrain;
import com.theodore.racingcore.entities.cars.ElectricMotor;
import com.theodore.racingcore.entities.cars.Engine;
import com.theodore.racingcore.entities.generalentries.TransmissionType;
import com.theodore.racingcore.exceptions.InvalidETagException;
import com.theodore.racingcore.mappers.DrivetrainMapper;
import com.theodore.racingcore.mappers.ElectricMotorMapper;
import com.theodore.racingcore.mappers.EngineMapper;
import com.theodore.racingcore.models.automobiles.drivetrain.requests.*;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.DrivetrainResponseDto;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.ElectricMotorResponseDto;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.EngineResponseDto;
import com.theodore.racingcore.repositories.DrivetrainRepository;
import com.theodore.racingcore.repositories.ElectricMotorRepository;
import com.theodore.racingcore.repositories.EngineRepository;
import com.theodore.racingcore.repositories.generalentries.TransmissionTypeRepository;
import com.theodore.racingcore.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DrivetrainServiceImpl implements DrivetrainService {

    private final EngineRepository engineRepository;
    private final ElectricMotorRepository electricMotorRepository;
    private final ManufacturerService manufacturerService;
    private final EngineMapper engineMapper;
    private final ElectricMotorMapper electricMotorMapper;
    private final DrivetrainMapper drivetrainMapper;
    private final DrivetrainRepository drivetrainRepository;
    private final TransmissionTypeRepository transmissionTypeRepository;

    public DrivetrainServiceImpl(EngineRepository engineRepository, ElectricMotorRepository electricMotorRepository,
                                 ManufacturerService manufacturerService, EngineMapper engineMapper,
                                 ElectricMotorMapper electricMotorMapper, DrivetrainMapper drivetrainMapper,
                                 DrivetrainRepository drivetrainRepository, TransmissionTypeRepository transmissionTypeRepository) {
        this.engineRepository = engineRepository;
        this.electricMotorRepository = electricMotorRepository;
        this.manufacturerService = manufacturerService;
        this.engineMapper = engineMapper;
        this.electricMotorMapper = electricMotorMapper;
        this.drivetrainMapper = drivetrainMapper;
        this.drivetrainRepository = drivetrainRepository;
        this.transmissionTypeRepository = transmissionTypeRepository;
    }

    @Override
    public DrivetrainResponseDto createNewDrivetrain(CreateNewDrivetrainRequestDto request) {

        var drivetrain = new Drivetrain();

        if (request.engine() == null && request.electricMotor() == null) {
            throw new RuntimeException("something went wrong");//todo: better exception
        }
        if (request.engine() != null) {
            var engine = getEngineById(request.engine());
            drivetrain.setEngine(engine);
        }

        if (request.electricMotor() != null) {
            var electricMotor = getElectricMotorById(request.electricMotor());
            drivetrain.setElectricMotor(electricMotor);
        }

        var transmission = getTransmissionType(request.transmission());
        drivetrain.setTransmissionType(transmission);
        drivetrain.setNumberOfGears(request.numberOfGears());

        var savedDrivetrain = saveDrivetrain(drivetrain);

        return drivetrainMapper.toResponse(savedDrivetrain);
    }

    @Override
    public DrivetrainResponseDto updateDrivetrain(Long id, UpdateDrivetrainRequestDto request, String ifMatch) {
        var drivetrain = getDrivetrainEntityById(id);

        if (drivetrain.getVersion() != Utils.parseIfMatch(ifMatch)) {
            throw new InvalidETagException();
        }

        if (request.engine() == null && request.electricMotor() == null) {
            throw new RuntimeException("something went wrong");//todo: better exception
        }

        if (checkIfDrivetrainAllFieldsUnchanged(request, drivetrain)) {
            return drivetrainMapper.toResponse(drivetrain);
        }

        if (request.engine() != null) {
            var engine = getEngineById(request.engine());
            drivetrain.setEngine(engine);
        } else {
            drivetrain.setEngine(null);
        }

        if (request.electricMotor() != null) {
            var electricMotor = getElectricMotorById(request.electricMotor());
            drivetrain.setElectricMotor(electricMotor);
        } else {
            drivetrain.setElectricMotor(null);
        }

        var transmission = getTransmissionType(request.transmission());
        drivetrain.setTransmissionType(transmission);
        drivetrain.setNumberOfGears(request.numberOfGears());

        var savedDrivetrain = saveDrivetrain(drivetrain);

        return drivetrainMapper.toResponse(savedDrivetrain);
    }

    @Override
    public EngineResponseDto insertNewEngine(CreateEngineRequestDto request) {
        var manufacturer = manufacturerService.getManufacturerByManufacturerId(request.manufacturerId());
        var newEngine = engineMapper.toEntity(request, manufacturer);
        var savedEngine = engineRepository.save(newEngine);
        return engineMapper.toResponse(savedEngine);
    }

    @Override
    public EngineResponseDto updateEngine(Long id, UpdateEngineRequestDto request, String ifMatch) {

        var engine = getEngineById(id);

        if (engine.getVersion() != Utils.parseIfMatch(ifMatch)) {
            throw new InvalidETagException();
        }

        if (!checkIfEngineAllFieldsUnchanged(request, engine)) {
            engineMapper.updateEntity(engine, request);
            engineRepository.save(engine);
        }

        return engineMapper.toResponse(engine);
    }

    private boolean checkIfEngineAllFieldsUnchanged(UpdateEngineRequestDto request, Engine engine) {
        return request.displacement().equals(engine.getDisplacement())
                && request.torque().equals(engine.getTorque())
                && request.motorCode().equals(engine.getMotorCode())
                && request.horsepower().equals(engine.getHorsepower());
    }

    private boolean checkIfDrivetrainAllFieldsUnchanged(UpdateDrivetrainRequestDto request, Drivetrain drivetrain) {
        Long currentEngineId = drivetrain.getEngine() != null ? drivetrain.getEngine().getId() : null;
        Long currentMotorId = drivetrain.getElectricMotor() != null ? drivetrain.getElectricMotor().getId() : null;
        String currentTransmissionCode = drivetrain.getTransmissionType() != null
                ? drivetrain.getTransmissionType().getSpecificCode()
                : null;

        return Objects.equals(request.engine(), currentEngineId)
                && Objects.equals(request.electricMotor(), currentMotorId)
                && Objects.equals(request.transmission(), currentTransmissionCode)
                && Objects.equals(request.numberOfGears(), drivetrain.getNumberOfGears());
    }

    @Override
    public ElectricMotorResponseDto insertNewElectricMotor(CreateElectricMotorRequestDto request) {
        var manufacturer = manufacturerService.getManufacturerByManufacturerId(request.manufacturerId());
        var newElectricMotor = electricMotorMapper.toEntity(request, manufacturer);

        var savedEngine = electricMotorRepository.save(newElectricMotor);
        return electricMotorMapper.toResponse(savedEngine);
    }


    @Override
    public Drivetrain saveDrivetrain(Drivetrain drivetrain) {
        return drivetrainRepository.save(drivetrain);
    }

    @Override
    public EngineResponseDto getEngineResponseById(Long id) {
        var engine = getEngineById(id);
        return engineMapper.toResponse(engine);
    }

    @Override
    public DrivetrainResponseDto getDrivetrainResponseDto(Drivetrain drivetrain) {
        return drivetrainMapper.toResponse(drivetrain);
    }

    @Override
    public TransmissionType getTransmissionType(String transmissionCode) {
        return transmissionTypeRepository.findBySpecificCode(transmissionCode)
                .orElseThrow(() -> new NotFoundException("Transmission type not found"));
    }

    @Override
    public Drivetrain getDrivetrainEntityById(Long id) {
        return drivetrainRepository.findById(id).orElseThrow(() -> new NotFoundException("Drivetrain not found"));
    }

    @Override
    public DrivetrainResponseDto getDrivetrainResponseById(Long id) {
        return drivetrainMapper.toResponse(getDrivetrainEntityById(id));
    }

    private Engine getEngineById(Long id) {
        return engineRepository.findById(id).orElseThrow(() -> new NotFoundException("Engine not found"));
    }

    private ElectricMotor getElectricMotorById(Long id) {
        return electricMotorRepository.findById(id).orElseThrow(() -> new NotFoundException("ElectricMotor not found"));
    }

}
