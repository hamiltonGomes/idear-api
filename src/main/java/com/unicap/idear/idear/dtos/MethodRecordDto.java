package com.unicap.idear.idear.dtos;
import jakarta.validation.constraints.NotBlank;

public record MethodRecordDto(
        @NotBlank
        String methodName,
        @NotBlank
        String methodDescription,
        @NotBlank
        String methodImage) {
}
