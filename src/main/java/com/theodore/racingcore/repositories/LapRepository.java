package com.theodore.racingcore.repositories;

import com.theodore.racingcore.entities.racing.Lap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LapRepository extends JpaRepository<Lap, Long> {
}
