package com.service.AuthRBAC.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


@RedisHash("allowList")
public class AllowToken implements Serializable {

    @Id
    private String refreshToken;

    private String accessToken;

    @Indexed
    private Long userId;

    @TimeToLive
    private Long expire;

    public AllowToken(String refreshToken, String accessToken, Long userId, Long expire) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.userId = userId;
        this.expire = expire;
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

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public Long expire() {
        return expire;
    }
}
