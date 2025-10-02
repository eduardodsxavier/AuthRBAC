package com.service.AuthRBAC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.AuthRBAC.model.Log;

public interface LogRepository extends JpaRepository<Log, Long>  {
    
}

