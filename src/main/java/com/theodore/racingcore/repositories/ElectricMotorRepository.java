package com.theodore.racingcore.repositories;

import com.theodore.racingcore.entities.cars.ElectricMotor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectricMotorRepository extends JpaRepository<ElectricMotor, Long> {
}
