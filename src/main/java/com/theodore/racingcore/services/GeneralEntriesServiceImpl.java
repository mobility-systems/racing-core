package com.theodore.racingcore.services;

import com.theodore.racingcore.entities.generalentries.GeneralCode;
import com.theodore.racingcore.models.general.CreateGeneralCodeRequestDto;
import com.theodore.racingcore.models.general.CreateGeneralEntryRequestDto;
import com.theodore.racingcore.repositories.generalentries.GeneralCodeRepository;
import com.theodore.racingcore.repositories.generalentries.GeneralEntryRepository;
import org.springframework.stereotype.Service;

@Service
public class GeneralEntriesServiceImpl implements GeneralEntriesService {

    private final GeneralCodeRepository generalCodeRepository;
    private final GeneralEntryRepository generalEntryRepository;
    private final GeneralEntryFactory generalEntryFactory;

    public GeneralEntriesServiceImpl(GeneralCodeRepository generalCodeRepository,
                                     GeneralEntryRepository generalEntryRepository,
                                     GeneralEntryFactory generalEntryFactory) {
        this.generalCodeRepository = generalCodeRepository;
        this.generalEntryRepository = generalEntryRepository;
        this.generalEntryFactory = generalEntryFactory;
    }

    @Override
    public void insertNewGeneralCode(CreateGeneralCodeRequestDto request) {
        if (checkGeneralCodeExists(request.codeId())) {
            throw new RuntimeException("General Code already exists");//todo different exception
        }
        GeneralCode generalCode = new GeneralCode();
        generalCode.setGeneralCodeId(request.codeId());
        generalCode.setDescription(request.description());
        generalCodeRepository.save(generalCode);
    }

    @Override
    public void insertNewGeneralEntry(CreateGeneralEntryRequestDto request) {
        var newGeneralEntry = generalEntryFactory.create(request);
        generalEntryRepository.save(newGeneralEntry);
    }

    private boolean checkGeneralCodeExists(String codeId) {
        return generalCodeRepository.existsById(codeId);
    }

}
