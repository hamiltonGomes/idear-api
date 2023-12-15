package com.unicap.idear.idear.dtos;

public record RegisterDto(String username, String email, String password, UserRoleDto role) {
}
