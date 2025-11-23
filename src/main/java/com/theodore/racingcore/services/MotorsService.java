package com.theodore.racingcore.services;

import com.theodore.racingcore.entities.cars.ElectricMotor;
import com.theodore.racingcore.entities.cars.Engine;

public interface MotorsService {

    Engine getEngineById(Long id);

    Engine saveEngine(Engine engine);

    ElectricMotor getElectricMotorById(Long id);

    ElectricMotor saveElectricMotor(ElectricMotor electricMotor);

}
