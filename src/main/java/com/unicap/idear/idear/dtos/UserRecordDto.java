package com.unicap.idear.idear.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UniqueElements;

public record UserRecordDto(
        @NotBlank @UniqueElements
        String username,
        @NotBlank @UniqueElements
        String email,
        @NotBlank
        String password,
        @NotBlank
        String role
            ) {
}
