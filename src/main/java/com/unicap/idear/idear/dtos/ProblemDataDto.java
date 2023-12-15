package com.unicap.idear.idear.dtos;

import jakarta.validation.constraints.NotBlank;

public record ProblemDataDto(
        @NotBlank
        String problemTitle,
        @NotBlank
        String problemDescription
) {
}
