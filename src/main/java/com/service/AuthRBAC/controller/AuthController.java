package com.service.AuthRBAC.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<Void> register()  {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login() {
        return new ResponseEntity<>(HttpStatus.OK);
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
