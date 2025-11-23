package com.theodore.racingcore.services;

import com.theodore.racingcore.entities.generalentries.BatteryType;
import com.theodore.racingcore.entities.generalentries.GeneralEntry;
import com.theodore.racingcore.entities.generalentries.HybridType;
import com.theodore.racingcore.entities.generalentries.TransmissionType;
import com.theodore.racingcore.models.general.CreateGeneralEntryRequestDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class GeneralEntryFactory {

    private final Map<String, Supplier<? extends GeneralEntry>> registry = Map.of(
            "BATTERY_TYPE", BatteryType::new,
            "HYBRID_TYPE", HybridType::new,
            "TRANSMISSION_TYPE", TransmissionType::new
    );

    public GeneralEntry create(CreateGeneralEntryRequestDto dto) {
        Supplier<? extends GeneralEntry> supplier = registry.get(dto.generalCode());
        if (supplier == null) {
            throw new IllegalArgumentException("Invalid general code: " + dto.generalCode());
        }
        GeneralEntry generalEntry = supplier.get();
        generalEntry.setSpecificCode(dto.specificCode());
        generalEntry.setDescription(dto.description());
        return generalEntry;
    }

}
