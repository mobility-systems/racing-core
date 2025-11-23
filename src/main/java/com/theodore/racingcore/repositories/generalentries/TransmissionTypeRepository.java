package com.theodore.racingcore.repositories.generalentries;


import com.theodore.racingcore.entities.generalentries.TransmissionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransmissionTypeRepository extends JpaRepository<TransmissionType, Long> {

    Optional<TransmissionType> findBySpecificCode(String specificCode);

}
