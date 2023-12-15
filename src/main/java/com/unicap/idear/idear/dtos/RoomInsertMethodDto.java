package com.unicap.idear.idear.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RoomInsertMethodDto(
        @Valid
        @NotNull
        long idMethod
) {

}
