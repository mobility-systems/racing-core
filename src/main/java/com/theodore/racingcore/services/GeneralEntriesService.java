package com.theodore.racingcore.services;

import com.theodore.racingcore.models.general.CreateGeneralCodeRequestDto;
import com.theodore.racingcore.models.general.CreateGeneralEntryRequestDto;

public interface GeneralEntriesService {

    void insertNewGeneralCode(CreateGeneralCodeRequestDto request);

    void insertNewGeneralEntry(CreateGeneralEntryRequestDto request);

}
