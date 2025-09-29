package com.service.AuthRBAC.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash("Token")
public class RefreshToken implements Serializable {

    @Id
    private String sessionId;

    private String token;

    public RefreshToken(String sessionId, String token) {
        this.sessionId = sessionId;
        this.token = token;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String sessionId() {
        return sessionId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String token() {
        return token;
    }
}
