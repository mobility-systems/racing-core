package com.theodore.racingcore.models.automobiles.manufacturers.responses;

import com.theodore.racingcore.models.automobiles.cars.responses.ManufacturerCarModelResponseDto;
import com.theodore.infrastructure.common.enums.Country;

import java.util.Set;

public record ManufacturerResponseDto(Long id,
                                      String name,
                                      Country country,
                                      Double reliabilityRating,
                                      Set<ManufacturerCarModelResponseDto> carModels,
                                      Long version) {
}
