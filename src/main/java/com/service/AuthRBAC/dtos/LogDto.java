package com.service.AuthRBAC.dtos;

import com.service.AuthRBAC.enums.Action;

public record LogDto(
    String username,
    boolean success,
    Action action
) {}
