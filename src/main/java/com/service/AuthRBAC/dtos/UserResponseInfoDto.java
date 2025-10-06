package com.service.AuthRBAC.dtos;

import com.service.AuthRBAC.enums.Role;

public record UserResponseInfoDto(
    Long userId,
    String username,
    Role role
) {}
