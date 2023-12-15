package com.unicap.idear.idear.dtos;

import jakarta.validation.constraints.NotNull;

public record IdeaSelectedDto(
        @NotNull
        Long idIdea
) {
}
