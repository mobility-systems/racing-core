package com.theodore.racingcore.models.automobiles.manufacturers.requests;

import com.theodore.infrastructure.common.entities.enums.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewManufacturerRequestDto(@NotNull @NotBlank String name,
                                        @NotNull Country country) {
}
