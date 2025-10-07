package com.service.AuthRBAC.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.awt.Canvas;

import org.springframework.beans.factory.annotation.Autowired;

import com.service.AuthRBAC.dtos.UserInfoDto;
import com.service.AuthRBAC.dtos.UserResponseInfoDto;
import com.service.AuthRBAC.dtos.AssignRoleDto;
import com.service.AuthRBAC.enums.Action;

import jakarta.servlet.http.HttpServletRequest;
import com.service.AuthRBAC.service.LogService;
import com.service.AuthRBAC.service.UserService;
import com.service.AuthRBAC.exception.InvalidRoleException;
import com.service.AuthRBAC.exception.InvalidTokenException;
import com.service.AuthRBAC.exception.UserNotFoundException;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    @Autowired
    private UserService service; 

    @Autowired
    private LogService log; 

    @GetMapping("/me")
    public ResponseEntity<UserResponseInfoDto> me(HttpServletRequest request)  {
        try {
            UserResponseInfoDto userInfo = service.UserInformation(request);
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } catch (Exception e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    @PostMapping("/assign-role")
    public ResponseEntity<Void> assignRole(@RequestBody AssignRoleDto newRoleInfo) {
        try {
            service.assignRole(newRoleInfo);

            log.save(newRoleInfo.username().toString(), true, Action.ASSIGN_ROLE);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.save(newRoleInfo.username().toString(), false, Action.ASSIGN_ROLE);
            throw new InvalidRoleException(e.getMessage());
        }
    }

    @PutMapping("update")
    public ResponseEntity<UserResponseInfoDto> updateUser(HttpServletRequest request, @RequestBody UserInfoDto newUserInfo) {
        try {
            service.updateUser(request, newUserInfo);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    @DeleteMapping("/delete/me")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
        try {
            service.deleteUser(request);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteUserByName(@PathVariable String username) {
        try {
            service.deleteUserByName(username);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }
}
