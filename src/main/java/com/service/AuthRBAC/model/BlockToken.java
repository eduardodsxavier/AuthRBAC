package com.service.AuthRBAC.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@RedisHash("blockList")
public class BlockToken implements Serializable {

    @Id
    private String token;

    private Long userId;

    @TimeToLive
    private Long expire;

    public BlockToken(String token, Long userId, Long expire) {
        this.token = token;
        this.userId = userId;
        this.expire = expire;
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

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public Long expire() {
        return expire;
    }
}
