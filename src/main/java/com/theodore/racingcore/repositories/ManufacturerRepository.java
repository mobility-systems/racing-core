package com.theodore.racingcore.repositories;

import com.theodore.racingcore.entities.cars.Manufacturer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long>, JpaSpecificationExecutor<Manufacturer> {

    @EntityGraph(attributePaths = "carModels")
    Optional<Manufacturer> getWithAllCarsById(Long id);

}
