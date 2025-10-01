package com.service.AuthRBAC.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("/audit-logs")
    public ResponseEntity<Void> AuditLogs()  {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
