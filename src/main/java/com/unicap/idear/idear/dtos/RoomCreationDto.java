package com.unicap.idear.idear.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomCreationDto(@NotBlank
                              String roomName,
                              @Valid
                              ProblemDataDto problemDataDto
) {
}
