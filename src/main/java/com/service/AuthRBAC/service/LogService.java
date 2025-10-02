package com.service.AuthRBAC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.AuthRBAC.dtos.LogDto;
import com.service.AuthRBAC.enums.Action;
import com.service.AuthRBAC.model.Log;
import com.service.AuthRBAC.repository.LogRepository;

@Service
public class LogService {

    @Autowired
    private LogRepository repository;

    public void login(LogDto logInfo) {
        Log log = new Log();
        log.setUsername(logInfo.username());
        log.setAction(Action.LOGIN);
        log.setSuccess(logInfo.success());
        repository.save(log);
    }
    

    public void register(LogDto logInfo) {
        Log log = new Log();
        log.setUsername(logInfo.username());
        log.setAction(Action.LOGIN);
        log.setSuccess(logInfo.success());
        repository.save(log);
    }

    public void refresh(LogDto logInfo) {
        Log log = new Log();
        log.setUsername(logInfo.username());
        log.setAction(Action.LOGIN);
        log.setSuccess(logInfo.success());
        repository.save(log);
    }

    public void logOut(LogDto logInfo) {
        Log log = new Log();
        log.setUsername(logInfo.username());
        log.setAction(Action.LOGIN);
        log.setSuccess(logInfo.success());
        repository.save(log);
    }

    public void assignRole(LogDto logInfo) {
        Log log = new Log();
        log.setUsername(logInfo.username());
        log.setAction(Action.LOGIN);
        log.setSuccess(logInfo.success());
        repository.save(log);
    }
}
