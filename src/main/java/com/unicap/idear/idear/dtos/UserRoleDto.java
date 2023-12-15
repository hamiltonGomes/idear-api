package com.unicap.idear.idear.dtos;

public enum UserRoleDto {
    ADMIN("admin"),
    USER("user");

    private String role;

    UserRoleDto(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
