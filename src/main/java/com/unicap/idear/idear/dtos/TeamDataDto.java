package com.unicap.idear.idear.dtos;

import jakarta.validation.constraints.NotBlank;

public record TeamDataDto(
        @NotBlank
        String teamName
) {
}
