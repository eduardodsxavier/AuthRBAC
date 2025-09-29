package com.service.AuthRBAC.model;

import java.io.Serializable;
import java.sql.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash("whiteList")
public class TokenWhiteList implements Serializable {

    @Id
    private String refreshToken;

    private Long userId;

    private Date expireDate;

    public TokenWhiteList(String refreshToken, Long userId, Date expireDate) {
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.expireDate = expireDate;
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

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Date expireDate() {
        return expireDate;
    }
}
