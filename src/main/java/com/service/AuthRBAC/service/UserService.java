package com.service.AuthRBAC.service;

import org.springframework.stereotype.Service;

import com.service.AuthRBAC.dtos.UserResponseInfoDto;
import com.service.AuthRBAC.exception.InvalidCredentialsException;
import com.service.AuthRBAC.dtos.AssignRoleDto;
import com.service.AuthRBAC.dtos.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.AuthRBAC.model.Users;
import com.service.AuthRBAC.repository.UsersRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtService jwtService;

    public UserResponseInfoDto UserInformation(HttpServletRequest request) {
        String username = jwtService.getSubjectFromToken(jwtService.recoveryToken(request));
        Users user = usersRepository.findByName(username).get();

        return new UserResponseInfoDto(user.id(), user.name(), user.role());
    }

    public void updateUser(HttpServletRequest request, UserInfoDto newUserInfo) {
        String username = jwtService.getSubjectFromToken(jwtService.recoveryToken(request));
        Users user = usersRepository.findByName(username).get();

        if (user.validatePassword(newUserInfo.password().trim())) {
            throw new InvalidCredentialsException();
        }

        user.setName(newUserInfo.username().trim());
        user.setPassword(newUserInfo.password().trim());

        usersRepository.save(user);
    }

    public void assignRole(AssignRoleDto newRoleInfo) {
        Users user = usersRepository.findByName(newRoleInfo.username()).get();
        user.setRole(newRoleInfo.role());

        usersRepository.save(user);
    }

    public void deleteUser(HttpServletRequest request) {
        String username = jwtService.getSubjectFromToken(jwtService.recoveryToken(request));
        Users user = usersRepository.findByName(username).get();

        usersRepository.delete(user);
    }

    public void deleteUserByName(String username) {
        Users user = usersRepository.findByName(username.trim()).get();
        usersRepository.delete(user);
    }
}
