package com.service.AuthRBAC.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash("blackList")
public class TokenBlackList implements Serializable {

    @Id
    private String refreshToken;

    private Long userId;

    public TokenBlackList(String refreshToken, Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String refreshToken() {
        return refreshToken;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long userId() {
        return userId;
    }
}
