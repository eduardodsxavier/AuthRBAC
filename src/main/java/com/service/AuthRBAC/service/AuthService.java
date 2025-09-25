package com.service.AuthRBAC.service;

import org.springframework.stereotype.Service;

import com.service.AuthRBAC.dtos.LoginDto;
import com.service.AuthRBAC.dtos.RegisterDto;
import com.service.AuthRBAC.dtos.AccessTokenDto;
import com.service.AuthRBAC.dtos.RefreshTokenDto;
import com.service.AuthRBAC.dtos.AssignRoleDto;
import com.service.AuthRBAC.dtos.UserInfoDto;
import com.service.AuthRBAC.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.service.AuthRBAC.model.Users;
import com.service.AuthRBAC.enums.Role;
import com.service.AuthRBAC.repository.UsersRepository;
import com.service.AuthRBAC.security.UserDetailsImpl;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository repository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtService jwtService;

    public AccessTokenDto authenticate(LoginDto loginInfo) {
        UsernamePasswordAuthenticationToken userAuthenticationToken = new UsernamePasswordAuthenticationToken(loginInfo.username(), loginInfo.password());

        Authentication authentication = manager.authenticate(userAuthenticationToken); 

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new AccessTokenDto(jwtService.generateToken(userDetails));
    }

    public AccessTokenDto register(RegisterDto registerInfo) {
        Users user = new Users();
        user.setName(registerInfo.username());
        user.setPassword(passwordEncoder.encode(registerInfo.password()));
        user.setRole(Role.admin);
        user.setEnabled(true);
        
        repository.save(user);

        return authenticate(new LoginDto(registerInfo.username(), registerInfo.password()));
    }

    public AccessTokenDto refresh(RefreshTokenDto refreshToken) {
        return new AccessTokenDto("");
    }

    public void logout() {
    }

    public UserInfoDto UserInformation(HttpServletRequest request) {
        String username = jwtService.getSubjectFromToken(jwtService.recoveryToken(request));
        Users user = repository.findByName(username).get();

        return new UserInfoDto(user.id(), user.name(), user.role());
    }

    public void assignRole(AssignRoleDto info) {
        Users user = repository.findById(info.userId()).get();
        user.setRole(info.role());

        repository.save(user);
    }

    public void auditLogs() {
    }
}
