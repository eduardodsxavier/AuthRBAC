package com.service.AuthRBAC.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.AuthRBAC.dtos.LogDto;
import com.service.AuthRBAC.model.Log;
import com.service.AuthRBAC.repository.LogRepository;

@Service
public class LogService {

    @Autowired
    private LogRepository repository;

    public void save(LogDto logInfo) {
        Log log = new Log();
        log.setTimeStamp(ZonedDateTime.now(ZoneId.of("GMT-3")).toInstant());
        log.setUsername(logInfo.username());
        log.setSuccess(logInfo.success());
        log.setAction(logInfo.action());
        repository.save(log);
    }
}
