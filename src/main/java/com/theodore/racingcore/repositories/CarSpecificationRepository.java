package com.theodore.racingcore.repositories;

import com.theodore.racingcore.entities.cars.CarSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarSpecificationRepository extends JpaRepository<CarSpecification, Long> {

    boolean existsByCar_Id(Long id);

}
