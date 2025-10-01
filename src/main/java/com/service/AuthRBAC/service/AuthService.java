package com.service.AuthRBAC.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import com.service.AuthRBAC.dtos.LoginDto;
import com.service.AuthRBAC.dtos.RefreshTokenDto;
import com.service.AuthRBAC.dtos.RegisterDto;
import com.service.AuthRBAC.dtos.TokenDto;
import com.service.AuthRBAC.dtos.AssignRoleDto;
import com.service.AuthRBAC.dtos.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.service.AuthRBAC.model.Users;
import com.service.AuthRBAC.model.TokenWhiteList;
import com.service.AuthRBAC.model.TokenBlackList;
import com.service.AuthRBAC.enums.Role;
import com.service.AuthRBAC.exception.InvalidCredentialsException;
import com.service.AuthRBAC.repository.TokenWhiteListRepository;
import com.service.AuthRBAC.repository.TokenBlackListRepository;
import com.service.AuthRBAC.repository.UsersRepository;
import com.service.AuthRBAC.security.UserDetailsImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenWhiteListRepository whiteListRepository;

    @Autowired
    private TokenBlackListRepository blackListRepository;

    @Autowired
    private JwtService jwtService;

    public TokenDto authenticate(LoginDto loginInfo) {
        UsernamePasswordAuthenticationToken userAuthenticationToken = new UsernamePasswordAuthenticationToken(loginInfo.username(), loginInfo.password());

        Authentication authentication = manager.authenticate(userAuthenticationToken); 

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String refreshToken = UUID.randomUUID().toString();
        String accessToken = jwtService.generateToken(userDetails);
        Instant expireDate = ZonedDateTime.now(ZoneId.of("GMT-3")).plusDays(7).toInstant();

        whiteListRepository.save(new TokenWhiteList(refreshToken, accessToken, userDetails.getId(), expireDate));   

        return new TokenDto(accessToken, refreshToken);
    }

    public TokenDto register(RegisterDto registerInfo) {
        Users user = new Users();
        user.setName(registerInfo.username());
        user.setPassword(passwordEncoder.encode(registerInfo.password()));
        user.setRole(Role.admin);
        user.setEnabled(true);
        
        usersRepository.save(user);

        return authenticate(new LoginDto(registerInfo.username(), registerInfo.password()));
    }

    public TokenDto refresh(RefreshTokenDto refreshToken) {
        Optional<TokenBlackList> securityFault = blackListRepository.findById(refreshToken.refreshToken()); 
        if (securityFault.isPresent()) {
            Users user = usersRepository.findById(securityFault.get().userId()).get();
            TokenWhiteList whiteList = whiteListRepository.findByUserId(user.id()).get();

            blackListRepository.save(new TokenBlackList(whiteList.accessToken(), user.id()));
            whiteListRepository.delete(whiteList);

            throw new InvalidCredentialsException();
        }


        System.out.println("3333");

        TokenWhiteList whiteList = whiteListRepository.findById(refreshToken.refreshToken()).get();
        whiteListRepository.delete(whiteList);

        if (ZonedDateTime.now(ZoneId.of("GMT-3")).toInstant().isAfter(whiteList.expireDate())) {
            throw new InvalidCredentialsException();
        }

        blackListRepository.save(new TokenBlackList(refreshToken.refreshToken(), whiteList.userId()));

        UserDetailsImpl userDetails = new UserDetailsImpl(usersRepository.findById(whiteList.userId()).get());

        String newRefreshToken = UUID.randomUUID().toString();
        String newAccessToken = jwtService.generateToken(userDetails);
        Instant expireDate = ZonedDateTime.now(ZoneId.of("GMT-3")).plusDays(7).toInstant();

        whiteListRepository.save(new TokenWhiteList(newRefreshToken, newAccessToken, userDetails.getUser().id(), expireDate));   

        return new TokenDto(newAccessToken, newRefreshToken);
    }

    public void logout(RefreshTokenDto refreshToken) {
        TokenWhiteList whiteList = whiteListRepository.findById(refreshToken.refreshToken()).get(); 
        blackListRepository.save(new TokenBlackList(whiteList.accessToken(), whiteList.userId()));

        whiteListRepository.delete(whiteList);
    }

    public UserInfoDto UserInformation(HttpServletRequest request) {
        String username = jwtService.getSubjectFromToken(jwtService.recoveryToken(request));
        Users user = usersRepository.findByName(username).get();

        return new UserInfoDto(user.id(), user.name(), user.role());
    }

    public void assignRole(AssignRoleDto newRoleInfo) {
        Users user = usersRepository.findById(newRoleInfo.userId()).get();
        user.setRole(newRoleInfo.role());

        usersRepository.save(user);
    }

    public void auditLogs() {
    }

    public Optional<String> readServletCookie(HttpServletRequest request){
      return Arrays.stream(request.getCookies())
        .filter(cookie->cookie.getName().equals("refreshToken"))
        .map(Cookie::getValue)
        .findAny();
    }
}
