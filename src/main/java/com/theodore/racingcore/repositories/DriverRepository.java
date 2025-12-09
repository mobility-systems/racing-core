package com.theodore.racingcore.repositories;

import com.theodore.racingcore.entities.racing.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
