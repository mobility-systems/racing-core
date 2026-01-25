package com.theodore.racingcore.models.racing.requests;

import jakarta.validation.constraints.NotBlank;

public record CreateNewDriverRequestDto(@NotBlank String alias) {
}
