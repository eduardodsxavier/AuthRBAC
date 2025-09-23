package com.service.AuthRBAC.dtos;

import com.service.AuthRBAC.enums.Role;

public record UserInfoDto(
    String username,
    Role role
) {}
