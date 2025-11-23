package com.theodore.racingcore.models.automobiles.cars.requests;

import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record IceTechnicalDetailsDto(@Digits(integer = 2, fraction = 2) BigDecimal cityConsumption,
                                     @Digits(integer = 2, fraction = 2) BigDecimal highwayConsumption) {
}
