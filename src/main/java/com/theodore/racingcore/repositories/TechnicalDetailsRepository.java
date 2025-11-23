package com.theodore.racingcore.repositories;

import com.theodore.racingcore.entities.cars.TechnicalDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicalDetailsRepository extends JpaRepository<TechnicalDetails, Long> {
}
