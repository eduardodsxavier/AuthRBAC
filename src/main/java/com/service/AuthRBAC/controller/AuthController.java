package com.service.AuthRBAC.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.AuthRBAC.enums.Action;
import com.service.AuthRBAC.exception.UserAlreadyExistException;
import com.service.AuthRBAC.exception.InvalidCredentialsException;
import com.service.AuthRBAC.dtos.UserInfoDto;
import com.service.AuthRBAC.dtos.RefreshTokenDto;
import com.service.AuthRBAC.dtos.TokensDto;
import com.service.AuthRBAC.dtos.AccessTokenDto;
import com.service.AuthRBAC.service.AuthService;
import com.service.AuthRBAC.service.JwtService;
import com.service.AuthRBAC.service.LogService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService service; 

    @Autowired
    private LogService log; 

    @Autowired
    private JwtService jwt; 

    @PostMapping("/register")
    public ResponseEntity<AccessTokenDto> register(@RequestBody UserInfoDto registerInfo, HttpServletResponse response)  {
        try {
            TokensDto token = service.register(registerInfo);

            Cookie cookie = new Cookie("refreshToken", token.RefreshToken());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            log.save(registerInfo.username(), true, Action.REGISTER);
            return new ResponseEntity<>(new AccessTokenDto(token.AccessToken()), HttpStatus.OK);
        } catch (Exception e) {
            log.save(registerInfo.username(), false, Action.REGISTER);
            throw new UserAlreadyExistException(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> login(@RequestBody UserInfoDto loginInfo, HttpServletResponse response) {
        try {
            TokensDto token = service.authenticate(loginInfo);

            Cookie cookie = new Cookie("refreshToken", token.RefreshToken());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            log.save(loginInfo.username(), true, Action.LOGIN);
            return new ResponseEntity<>(new AccessTokenDto(token.AccessToken()), HttpStatus.OK);
        } catch (Exception e) {
            log.save(loginInfo.username(), false, Action.LOGIN);
            throw new InvalidCredentialsException(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenDto> refresh(HttpServletResponse response, HttpServletRequest request) {
        try {
            RefreshTokenDto refreshToken = new RefreshTokenDto(service.readRefreshToken(request).get());

            TokensDto token = service.refresh(refreshToken);

            Cookie cookie = new Cookie("refreshToken", token.RefreshToken());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            String username = jwt.getSubjectFromToken(token.AccessToken());

            log.save(username, true, Action.REFRESH);
            return new ResponseEntity<>(new AccessTokenDto(token.AccessToken()), HttpStatus.OK);
        } catch (Exception e) {
            log.save("", false, Action.REFRESH);
            throw new InvalidCredentialsException(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        try {
            RefreshTokenDto refreshToken = new RefreshTokenDto(service.readRefreshToken(request).get());
            String username = service.logout(refreshToken);

            log.save(username, true, Action.LOG_OUT);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.save("", false, Action.LOG_OUT);
            throw new InvalidCredentialsException(e.getMessage());
        }
    }
}
