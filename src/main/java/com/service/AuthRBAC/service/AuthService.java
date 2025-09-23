package com.service.AuthRBAC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.AuthRBAC.dtos.LoginDto;
import com.service.AuthRBAC.dtos.RegisterDto;
import com.service.AuthRBAC.dtos.AccessTokenDto;
import com.service.AuthRBAC.dtos.RefreshTokenDto;
import com.service.AuthRBAC.model.Users;
import com.service.AuthRBAC.enums.Role;
import com.service.AuthRBAC.repository.UsersRepository;

@Service
public class AuthService {

    @Autowired
    private UsersRepository repository;

    public AccessTokenDto authenticate(LoginDto loginInfo) {
        return new AccessTokenDto("");
    }

    public AccessTokenDto register(RegisterDto registerInfo) {
        return new AccessTokenDto("");
    }

    public AccessTokenDto refresh(RefreshTokenDto refreshToken) {
        return new AccessTokenDto("");
    }

    public void logout() {
    }

    public Users UserInformation() {
        return new Users(1L, "", "", Role.viewer, false);
    }

    public void assignRole() {
    }

    public void auditLogs() {
    }
}
