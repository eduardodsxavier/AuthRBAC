package com.service.AuthRBAC.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.AuthRBAC.dtos.UserInfoDto;
import com.service.AuthRBAC.dtos.AssignRoleDto;

import jakarta.servlet.http.HttpServletRequest;
import com.service.AuthRBAC.service.AuthService;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private AuthService service; 

    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> me(HttpServletRequest request)  {
        UserInfoDto userInfo = service.UserInformation(request);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @PostMapping("/assign-role")
    public ResponseEntity<Void> assigbRole(@RequestBody AssignRoleDto newRoleInfo) {
        service.assignRole(newRoleInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
