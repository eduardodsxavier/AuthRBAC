package com.service.AuthRBAC.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.AuthRBAC.dtos.RegisterDto;
import com.service.AuthRBAC.enums.Action;
import com.service.AuthRBAC.exception.UserAlreadyExistException;
import com.service.AuthRBAC.exception.InvalidCredentialsException;
import com.service.AuthRBAC.dtos.LoginDto;
import com.service.AuthRBAC.dtos.RefreshTokenDto;
import com.service.AuthRBAC.dtos.TokenDto;
import com.service.AuthRBAC.dtos.AccessTokenDto;
import com.service.AuthRBAC.dtos.LogDto;
import com.service.AuthRBAC.service.AuthService;
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

    @PostMapping("/register")
    public ResponseEntity<AccessTokenDto> register(@RequestBody RegisterDto registerInfo, HttpServletResponse response)  {
        try {
            TokenDto token = service.register(registerInfo);

            Cookie cookie = new Cookie("refreshToken", token.RefreshToken());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            log.save(new LogDto(registerInfo.username(), true, Action.REGISTER));
            return new ResponseEntity<>(new AccessTokenDto(token.AccessToken()), HttpStatus.OK);
        } catch (Exception e) {
            log.save(new LogDto(registerInfo.username(), true, Action.REGISTER));
            throw new UserAlreadyExistException(registerInfo.username());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> login(@RequestBody LoginDto loginInfo, HttpServletResponse response) {
        try {
            TokenDto token = service.authenticate(loginInfo);

            Cookie cookie = new Cookie("refreshToken", token.RefreshToken());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            log.save(new LogDto(loginInfo.username(), true, Action.LOGIN));
            return new ResponseEntity<>(new AccessTokenDto(token.AccessToken()), HttpStatus.OK);
        } catch (Exception e) {
            log.save(new LogDto(loginInfo.username(), false, Action.LOGIN));
            throw new InvalidCredentialsException();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenDto> refresh(HttpServletResponse response, HttpServletRequest request) {
        try {
            RefreshTokenDto refreshToken = new RefreshTokenDto(service.readServletCookie(request).get());

            TokenDto token = service.refresh(refreshToken);

            Cookie cookie = new Cookie("refreshToken", token.RefreshToken());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            log.save(new LogDto("", true, Action.REFRESH));
            return new ResponseEntity<>(new AccessTokenDto(token.AccessToken()), HttpStatus.OK);
        } catch (Exception e) {
            log.save(new LogDto("", false, Action.REFRESH));
            throw new InvalidCredentialsException();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenDto refreshToken) {
        try {
            service.logout(refreshToken);

            log.save(new LogDto("", true, Action.LOG_OUT));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.save(new LogDto("", false, Action.LOG_OUT));
            throw new InvalidCredentialsException();
        }
    }

}
