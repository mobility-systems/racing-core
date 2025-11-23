package com.theodore.racingcore.models.general;

import jakarta.validation.constraints.NotBlank;

public record CreateGeneralEntryRequestDto(@NotBlank String generalCode,
                                           @NotBlank String specificCode,
                                           String description) {
}
