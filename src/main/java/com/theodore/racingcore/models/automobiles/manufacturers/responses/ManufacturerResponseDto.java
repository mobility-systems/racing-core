package com.theodore.racingcore.models.automobiles.manufacturers.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theodore.racingcore.models.automobiles.cars.responses.ManufacturerCarModelResponseDto;
import com.theodore.infrastructure.common.entities.enums.Country;

import java.util.Set;

public record ManufacturerResponseDto(Long id,
                                      String name,
                                      Country country,
                                      Double reliabilityRating,
                                      Set<ManufacturerCarModelResponseDto> carModels,
                                      @JsonIgnore long version) {
}
