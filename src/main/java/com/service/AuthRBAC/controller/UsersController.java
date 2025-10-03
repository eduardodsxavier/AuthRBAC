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
import com.service.AuthRBAC.dtos.LogDto;

import com.service.AuthRBAC.enums.Action;

import jakarta.servlet.http.HttpServletRequest;
import com.service.AuthRBAC.service.AuthService;
import com.service.AuthRBAC.service.LogService;
import com.service.AuthRBAC.exception.InvalidRoleException;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    @Autowired
    private AuthService service; 


    @Autowired
    private LogService log; 

    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> me(HttpServletRequest request)  {
        UserInfoDto userInfo = service.UserInformation(request);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @PostMapping("/assign-role")
    public ResponseEntity<Void> assignRole(@RequestBody AssignRoleDto newRoleInfo) {
        try {
            service.assignRole(newRoleInfo);

            log.save(new LogDto(newRoleInfo.userId().toString(), true, Action.ASSIGN_ROLE));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.save(new LogDto(newRoleInfo.userId().toString(), false, Action.ASSIGN_ROLE));
            throw new InvalidRoleException(newRoleInfo.role().toString());
        }
    }
}
