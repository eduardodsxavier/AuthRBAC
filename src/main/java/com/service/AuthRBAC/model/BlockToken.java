package com.service.AuthRBAC.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash("blockList")
public class BlockToken implements Serializable {

    @Id
    private String token;

    private Long userId;

    public BlockToken(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public void setToken(String refreshToken) {
        this.token = refreshToken;
    }

    public String token() {
        return token;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long userId() {
        return userId;
    }
}
