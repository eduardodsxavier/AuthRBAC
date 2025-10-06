package com.service.AuthRBAC.dtos;

public record TokensDto(
    String AccessToken,
    String RefreshToken
) {}
