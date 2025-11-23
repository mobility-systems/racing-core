package com.theodore.racingcore.models.automobiles.cars.requests;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record HybridDetailsDto(@Size(max = 16) String hybridTypeCode,
                               @Digits(integer = 2, fraction = 2) BigDecimal cityConsumption,
                               @Digits(integer = 2, fraction = 2) BigDecimal highwayConsumption,
                               @Min(0) Integer electricRange
) {
}
