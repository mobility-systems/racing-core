package com.theodore.racingcore.services;

import com.theodore.infrastructure.common.exceptions.NotFoundException;
import com.theodore.infrastructure.common.exceptions.ReferenceMismatchException;
import com.theodore.racingcore.entities.cars.CarModel;
import com.theodore.racingcore.entities.cars.CarSpecification;
import com.theodore.racingcore.exceptions.InvalidETagException;
import com.theodore.racingcore.mappers.CarModelMapper;
import com.theodore.racingcore.mappers.TechnicalDetailsMapper;
import com.theodore.racingcore.models.automobiles.cars.requests.*;
import com.theodore.racingcore.models.automobiles.cars.responses.CarModelResponseDto;
import com.theodore.racingcore.models.automobiles.cars.responses.CarResponseDto;
import com.theodore.racingcore.repositories.CarModelRepository;
import com.theodore.racingcore.repositories.CarSpecificationRepository;
import com.theodore.racingcore.repositories.TechnicalDetailsRepository;
import com.theodore.racingcore.utils.CacheNames;
import com.theodore.racingcore.utils.Utils;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService {

    private static final String CAR_NOT_FOUND= "Car not found";

    private final CarSpecificationRepository carSpecificationRepository;
    private final ManufacturerService manufacturerService;
    private final CarModelRepository carModelRepository;
    private final CarModelMapper carModelMapper;
    private final DrivetrainService drivetrainService;
    private final TechnicalDetailsMapper technicalDetailsMapper;
    private final TechnicalDetailsRepository technicalDetailsRepository;

    public CarServiceImpl(CarSpecificationRepository carSpecificationRepository, ManufacturerService manufacturerService,
                          CarModelRepository carModelRepository, CarModelMapper carModelMapper,
                          DrivetrainService drivetrainService, TechnicalDetailsMapper technicalDetailsMapper,
                          TechnicalDetailsRepository technicalDetailsRepository) {
        this.carSpecificationRepository = carSpecificationRepository;
        this.manufacturerService = manufacturerService;
        this.carModelRepository = carModelRepository;
        this.carModelMapper = carModelMapper;
        this.drivetrainService = drivetrainService;
        this.technicalDetailsMapper = technicalDetailsMapper;
        this.technicalDetailsRepository = technicalDetailsRepository;
    }

    @Override
    public CarModelResponseDto createNewCarModel(CarModelRequestDto request) {
        var manufacturer = manufacturerService.getManufacturerByManufacturerId(request.manufacturerId());
        var carModel = carModelMapper.dtoToEntity(request, manufacturer);
        var savedCarModel = carModelRepository.save(carModel);
        return carModelMapper.toResponse(savedCarModel);
    }

    @Override
    @CachePut(cacheNames = CacheNames.CAR_MODEL_BY_ID, key = "#id")
    public CarModelResponseDto updateCarModel(Long id, CarModelRequestDto request, String ifMatch) {
        var carModel = getCarModelById(id);

        if (carModel.getVersion() != Utils.parseIfMatch(ifMatch)) {
            throw new InvalidETagException();
        }

        var manufacturer = manufacturerService.getManufacturerByManufacturerId(request.manufacturerId());

        carModelMapper.updateEntity(carModel, request, manufacturer);

        var updatedCarModel = carModelRepository.save(carModel);

        return carModelMapper.toResponse(updatedCarModel);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.CAR_MODEL_BY_ID, key = "#id")
    public void deleteCarModel(Long id, String ifMatch) {
        if (carSpecificationRepository.existsByCar_Id(id)) {
            throw new ReferenceMismatchException("Cannot delete car model because it is used");
        }

        var carModel = getCarModelById(id);

        if (carModel.getVersion() != Utils.parseIfMatch(ifMatch)) {
            throw new InvalidETagException();
        }

        carModelRepository.delete(carModel);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.CAR_MODEL_BY_ID, key = "#id", unless = "#result == null")
    public CarModelResponseDto findCarModelById(Long id) {
        var carModel = getCarModelById(id);
        return carModelMapper.toResponse(carModel);
    }

    @Override
    public CarResponseDto insertNewCarInfo(CreateNewCarRequest request) {
        return switch (request) {
            case CreateNewIceCarRequestDto ice -> insertNewIceCarInfo(ice);
            case CreateNewHybridCarRequestDto hybrid -> createNewHybridCarInfo(hybrid);
            case CreateNewElectricCarRequestDto bev -> createNewElectricCarInfo(bev);
        };
    }

    @Override
    @CachePut(cacheNames = CacheNames.ALL_CAR_INFO_BY_ID, key = "#id")
    public CarResponseDto updateCar(Long id, UpdateCarRequest updateRequest, String ifMatch) {

        var carSpecification = carSpecificationRepository.findById(id).orElseThrow(() -> new NotFoundException(CAR_NOT_FOUND));

        if (carSpecification.getVersion() != Utils.parseIfMatch(ifMatch)) {
            throw new InvalidETagException();
        }

        return switch (updateRequest) {
            case UpdateIceCarRequestDto ice -> updateIceCarInfo(carSpecification, ice);
            case UpdateHybridCarRequestDto hybrid -> updateHybridCarInfo(carSpecification, hybrid);
            case UpdateElectricCarRequestDto bev -> updateElectricCarInfo(carSpecification, bev);
        };
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.ALL_CAR_INFO_BY_ID, key = "#id")
    public void deleteCar(Long id, String ifMatch) {
        var car = carSpecificationRepository.findById(id).orElseThrow(() -> new NotFoundException(CAR_NOT_FOUND));

        if (car.getVersion() != Utils.parseIfMatch(ifMatch)) {
            throw new InvalidETagException();
        }

        carSpecificationRepository.delete(car);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.ALL_CAR_INFO_BY_ID, key = "#id", unless = "#result == null")
    public CarResponseDto getCarById(Long id) {
        var carSpecification = carSpecificationRepository.findById(id).orElseThrow(() -> new NotFoundException(CAR_NOT_FOUND));
        var carModelResponse = carModelMapper.toResponse(carSpecification.getCar());
        var drivetrainResponse = drivetrainService.getDrivetrainResponseDto(carSpecification.getDrivetrain());
        var technicalDetailsResponse = technicalDetailsMapper.toResponse(carSpecification.getTechnicalDetails());
        return new CarResponseDto(carSpecification.getId(),
                carModelResponse,
                drivetrainResponse,
                technicalDetailsResponse,
                carSpecification.getVersion()
        );
    }

    private CarResponseDto insertNewIceCarInfo(CreateNewIceCarRequestDto request) {
        var carSpecification = new CarSpecification();

        var carModel = getCarModelById(request.carModel());
        carSpecification.setCar(carModel);

        var drivetrain = drivetrainService.getDrivetrainEntityById(request.drivetrain());
        carSpecification.setDrivetrain(drivetrain);

        var savedCarSpecs = carSpecificationRepository.save(carSpecification);

        var techDeets = technicalDetailsMapper.newIceCarDtoToEntity(request.technicalDetails(), request.iceTechnicalDetails(), savedCarSpecs);

        var savedTechDeets = technicalDetailsRepository.save(techDeets);

        savedCarSpecs.setTechnicalDetails(savedTechDeets);

        var carModelResponse = carModelMapper.toResponse(savedCarSpecs.getCar());
        var drivetrainResponse = drivetrainService.getDrivetrainResponseDto(savedCarSpecs.getDrivetrain());
        var technicalDetailsResponse = technicalDetailsMapper.toResponse(savedCarSpecs.getTechnicalDetails());

        return new CarResponseDto(savedCarSpecs.getId(),
                carModelResponse,
                drivetrainResponse,
                technicalDetailsResponse,
                savedCarSpecs.getVersion()
        );

    }

    //todo
    private CarResponseDto createNewHybridCarInfo(CreateNewHybridCarRequestDto request) {
        return null;
    }

    //todo
    private CarResponseDto createNewElectricCarInfo(CreateNewElectricCarRequestDto request) {
        return null;
    }

    private CarResponseDto updateIceCarInfo(CarSpecification carSpecification, UpdateIceCarRequestDto updateRequest) {

        if (carSpecification.getDrivetrain() == null
                || !updateRequest.drivetrain().equals(carSpecification.getDrivetrain().getId())) {
            var newDrivetrain = drivetrainService.getDrivetrainEntityById(updateRequest.drivetrain());
            drivetrainService.saveDrivetrain(newDrivetrain);
            carSpecification.setDrivetrain(newDrivetrain);
        }
        if (carSpecification.getTechnicalDetails() == null) {
            var techDeets = technicalDetailsMapper.newIceCarDtoToEntity(updateRequest.technicalDetails(),
                    updateRequest.iceTechnicalDetails(),
                    carSpecification);
            technicalDetailsRepository.save(techDeets);
            carSpecification.setTechnicalDetails(techDeets);
        } else {
            var techDeets = carSpecification.getTechnicalDetails();
            technicalDetailsMapper.updateIceCarEntity(techDeets,
                    updateRequest.technicalDetails(),
                    updateRequest.iceTechnicalDetails());
            technicalDetailsRepository.save(techDeets);
            carSpecification.setTechnicalDetails(techDeets);
        }
        var updatedCarSpecs = carSpecificationRepository.save(carSpecification);

        var carModelResponse = carModelMapper.toResponse(updatedCarSpecs.getCar());
        var drivetrainResponse = drivetrainService.getDrivetrainResponseDto(updatedCarSpecs.getDrivetrain());
        var technicalDetailsResponse = technicalDetailsMapper.toResponse(updatedCarSpecs.getTechnicalDetails());

        return new CarResponseDto(updatedCarSpecs.getId(),
                carModelResponse,
                drivetrainResponse,
                technicalDetailsResponse,
                updatedCarSpecs.getVersion());
    }

    //todo
    private CarResponseDto updateHybridCarInfo(CarSpecification carSpecification, UpdateHybridCarRequestDto updateRequest) {
        return null;
    }

    //todo
    private CarResponseDto updateElectricCarInfo(CarSpecification carSpecification, UpdateElectricCarRequestDto updateRequest) {
        return null;
    }

    private CarModel getCarModelById(Long id) {
        return carModelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Car Model not found"));
    }

}
