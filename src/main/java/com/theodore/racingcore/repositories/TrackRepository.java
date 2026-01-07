package com.theodore.racingcore.repositories;

import com.theodore.infrastructure.common.enums.Country;
import com.theodore.racingcore.entities.racing.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {

    boolean existsByTrackNameIgnoreCaseAndCountry(String trackName, Country country);

}
