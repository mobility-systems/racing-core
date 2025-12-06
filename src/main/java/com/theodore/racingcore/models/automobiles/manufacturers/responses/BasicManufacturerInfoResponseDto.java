package com.theodore.racingcore.models.automobiles.manufacturers.responses;

import com.theodore.infrastructure.common.enums.Country;

public record BasicManufacturerInfoResponseDto(String name,
                                               Country country) {
}
