package com.theodore.racingcore.models.automobiles.cars.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theodore.racingcore.models.automobiles.manufacturers.responses.BasicManufacturerInfoResponseDto;

public record CarModelResponseDto(Long id,
                                  String carName,
                                  Integer modelYear,
                                  Integer generation,
                                  Integer facelift,
                                  BasicManufacturerInfoResponseDto manufacturer,
                                  String internalCode,
                                  Integer doors,
                                  @JsonIgnore long version) {
}
