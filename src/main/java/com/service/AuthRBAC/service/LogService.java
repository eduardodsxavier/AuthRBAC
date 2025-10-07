package com.service.AuthRBAC.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.AuthRBAC.dtos.LogDto;
import com.service.AuthRBAC.enums.Action;
import com.service.AuthRBAC.model.Log;
import com.service.AuthRBAC.repository.LogRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LogService {

    @Autowired
    private LogRepository repository;

    @Autowired
    private JwtService jwtService;

    public void save(String username, boolean success, Action action) {
        Log log = new Log();
        log.setTimeStamp(ZonedDateTime.now(ZoneId.of("GMT-3")).toInstant());
        log.setUsername(username);
        log.setSuccess(success);
        log.setAction(action);
        repository.save(log);
    }

    public List<LogDto> auditLogs() {
        List<LogDto> logs = new ArrayList<>();
        repository.findAll().stream().forEach(log -> logs.add(new LogDto(log.timeStamp(), log.username(), log.action(), log.success())));
        return logs;
    }

    public List<LogDto> userAuditLogs(HttpServletRequest request) {
        String username = jwtService.getSubjectFromToken(jwtService.recoveryToken(request));

        List<LogDto> logs = new ArrayList<>();
        repository.findByUsername(username).get().stream().forEach(log -> logs.add(new LogDto(log.timeStamp(), log.username(), log.action(), log.success())));
        return logs;
    }
}
