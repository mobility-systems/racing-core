package com.theodore.racingcore.repositories;

import com.theodore.racingcore.entities.cars.Drivetrain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivetrainRepository extends JpaRepository<Drivetrain, Long> {
}
