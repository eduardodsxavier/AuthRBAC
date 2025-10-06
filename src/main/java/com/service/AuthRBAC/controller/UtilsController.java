package com.service.AuthRBAC.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.service.AuthRBAC.dtos.LogDto;
import com.service.AuthRBAC.service.LogService;


@RestController
@RequestMapping("/api/v1")
public class UtilsController {

    @Autowired
    private LogService service;

    @GetMapping("/audit-logs")
    public List<LogDto> AuditLogs()  {
        try {
            return service.auditLogs();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Void> health()  {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Void> roles()  {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
