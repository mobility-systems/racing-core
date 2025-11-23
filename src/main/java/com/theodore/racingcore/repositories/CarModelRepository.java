package com.theodore.racingcore.repositories;

import com.theodore.racingcore.entities.cars.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
}
