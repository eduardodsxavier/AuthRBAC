package com.service.AuthRBAC.model;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;

import com.service.AuthRBAC.enums.Action;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @CreatedDate
    private Instant timeStamp;

    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Action action;

    private boolean success;

    public Log() {}

    public Log(Long id, Instant timeStamp, String username, Action action, boolean success) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.action = action;
        this.username = username;
        this.success = success;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Instant timeStamp() {
        return timeStamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Action action() {
        return action;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public boolean success() {
        return success;
    }
}
