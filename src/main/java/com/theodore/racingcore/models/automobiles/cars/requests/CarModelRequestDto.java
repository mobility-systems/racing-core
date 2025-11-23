package com.theodore.racingcore.models.automobiles.cars.requests;

import jakarta.validation.constraints.*;

public record CarModelRequestDto(@NotBlank @Size(max = 255) String carName,
                                 @NotNull @Min(1886) Integer modelYear,
                                 @NotNull @Min(1) Integer generation,
                                 @Min(0) Integer facelift,
                                 @NotNull Long manufacturerId,
                                 @Size(max = 50) String internalCode,
                                 @NotNull @Min(2) @Max(6) Integer doors) {
}
