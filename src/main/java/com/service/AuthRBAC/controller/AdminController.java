package com.service.AuthRBAC.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.service.AuthRBAC.dtos.LogResponseDto;
import com.service.AuthRBAC.service.AuthService;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AuthService service;

    @GetMapping("/audit-logs")
    public List<LogResponseDto> AuditLogs()  {
        List<LogResponseDto> logs = new ArrayList<>();
        service.auditLogs().stream().forEach(log -> logs.add(new LogResponseDto(log.timeStamp(), log.username(), log.action(), log.success())));
        return logs;
    }
}
