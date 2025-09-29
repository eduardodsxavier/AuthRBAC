package com.service.AuthRBAC.dtos;

public record TokenDto(
    String AccessToken,
    String RefreshToken
) {}
