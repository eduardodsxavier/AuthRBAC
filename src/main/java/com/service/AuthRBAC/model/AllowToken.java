package com.service.AuthRBAC.model;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@RedisHash("allowList")
public class AllowToken implements Serializable {

    @Id
    private String refreshToken;

    private String accessToken;

    @Indexed
    private Long userId;

    private Instant expireDate;

    public AllowToken(String refreshToken, String accessToken, Long userId, Instant expireDate) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.userId = userId;
        this.expireDate = expireDate;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String refreshToken() {
        return refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String accessToken() {
        return accessToken;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long userId() {
        return userId;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public Instant expireDate() {
        return expireDate;
    }
}
