package com.service.AuthRBAC.dtos;

import java.time.Instant;

import com.service.AuthRBAC.enums.Action;

public record LogDto(
    Instant timeStamp,
    String username,
    Action action,
    boolean success
) {}
