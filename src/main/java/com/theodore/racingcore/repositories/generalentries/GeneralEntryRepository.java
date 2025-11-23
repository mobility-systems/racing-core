package com.theodore.racingcore.repositories.generalentries;

import com.theodore.racingcore.entities.generalentries.GeneralEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneralEntryRepository extends JpaRepository<GeneralEntry, Long> {

    Optional<GeneralEntry> findByGeneralCode_GeneralCodeIdAndSpecificCode(String generalCodeId, String specificCode);

}
