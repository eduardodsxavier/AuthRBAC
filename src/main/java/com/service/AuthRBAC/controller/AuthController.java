package com.service.AuthRBAC.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.AuthRBAC.dtos.RegisterDto;
import com.service.AuthRBAC.exception.UserAlreadyExistException;
import com.service.AuthRBAC.exception.InvalidCredentialsException;
import com.service.AuthRBAC.dtos.LoginDto;
import com.service.AuthRBAC.dtos.AccessTokenDto;
import com.service.AuthRBAC.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service; 

    @PostMapping("/register")
    public ResponseEntity<AccessTokenDto> register(@RequestBody RegisterDto registerInfo)  {
        try {
            AccessTokenDto token = service.register(registerInfo);

            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            throw new UserAlreadyExistException(registerInfo.username());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> login(@RequestBody LoginDto loginInfo) {
        try {
            AccessTokenDto token = service.authenticate(loginInfo);

            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
