package com.theodore.racingcore.services;

import com.theodore.racingcore.entities.cars.ElectricMotor;
import com.theodore.racingcore.entities.cars.Engine;
import com.theodore.racingcore.repositories.ElectricMotorRepository;
import com.theodore.racingcore.repositories.EngineRepository;
import com.theodore.infrastructure.common.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MotorsServiceImpl implements MotorsService {

    private final EngineRepository engineRepository;
    private final ElectricMotorRepository electricMotorRepository;

    public MotorsServiceImpl(EngineRepository engineRepository, ElectricMotorRepository electricMotorRepository) {
        this.engineRepository = engineRepository;
        this.electricMotorRepository = electricMotorRepository;
    }

    @Override
    public Engine getEngineById(Long id) {
        return engineRepository.findById(id).orElseThrow(() -> new NotFoundException("Engine not found"));
    }

    @Override
    public Engine saveEngine(Engine engine) {
        return engineRepository.save(engine);
    }

    @Override
    public ElectricMotor getElectricMotorById(Long id) {
        return electricMotorRepository.findById(id).orElseThrow(() -> new NotFoundException("ElectricMotor not found"));
    }

    @Override
    public ElectricMotor saveElectricMotor(ElectricMotor electricMotor) {
        return electricMotorRepository.save(electricMotor);
    }
}
