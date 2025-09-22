package com.service.AuthRBAC.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/users")
public class UsersController {

    @GetMapping("/me")
    public ResponseEntity<Void> me()  {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/assign-role")
    public ResponseEntity<Void> assigbRole() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
