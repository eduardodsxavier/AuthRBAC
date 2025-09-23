package com.service.AuthRBAC.dtos;

import com.service.AuthRBAC.enums.Role;

public record AssignRoleDto(
    Long userId,
    Role role 
) {}
