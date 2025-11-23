package com.theodore.racingcore.repositories;

import com.theodore.racingcore.entities.cars.Engine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EngineRepository extends JpaRepository<Engine, Long> {
}
