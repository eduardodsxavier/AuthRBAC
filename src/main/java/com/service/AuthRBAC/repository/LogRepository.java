package com.service.AuthRBAC.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.AuthRBAC.model.Log;

public interface LogRepository extends JpaRepository<Log, Long>  {
    
    Optional<List<Log>> findByUsername(String username);
}

