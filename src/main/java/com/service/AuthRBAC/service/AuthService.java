package com.service.AuthRBAC.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import com.service.AuthRBAC.dtos.RefreshTokenDto;
import com.service.AuthRBAC.dtos.TokensDto;
import com.service.AuthRBAC.dtos.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import com.service.AuthRBAC.model.Users;
import com.service.AuthRBAC.model.AllowToken;
import com.service.AuthRBAC.model.BlockToken;
import com.service.AuthRBAC.enums.Role;
import com.service.AuthRBAC.exception.InvalidCredentialsException;
import com.service.AuthRBAC.repository.TokenAllowListRepository;
import com.service.AuthRBAC.repository.TokenBlockListRepository;
import com.service.AuthRBAC.repository.UsersRepository;
import com.service.AuthRBAC.security.UserDetailsImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenAllowListRepository allowListRepository;

    @Autowired
    private TokenBlockListRepository blockListRepository;

    @Autowired
    private JwtService jwtService;

    public TokensDto authenticate(UserInfoDto loginInfo) {
        UsernamePasswordAuthenticationToken userAuthenticationToken = new UsernamePasswordAuthenticationToken(loginInfo.username().trim(), loginInfo.password().trim());

        Authentication authentication = manager.authenticate(userAuthenticationToken); 

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String refreshToken = UUID.randomUUID().toString();
        String accessToken = jwtService.generateToken(userDetails);

        allowListRepository.save(new AllowToken(refreshToken, accessToken, userDetails.getId(), Duration.ofDays(7).toSeconds()));

        return new TokensDto(accessToken, refreshToken);
    }

    public TokensDto register(UserInfoDto registerInfo) {
        Users user = new Users();
        if (user.validatePassword(registerInfo.password().trim())) {
            throw new InvalidCredentialsException();
        }
        user.setName(registerInfo.username().trim());
        user.setPassword(registerInfo.password().trim());
        user.setRole(Role.admin);
        user.setEnabled(true);
        
        usersRepository.save(user);

        return authenticate(new UserInfoDto(registerInfo.username(), registerInfo.password()));
    }

    public TokensDto refresh(RefreshTokenDto refreshToken) {
        Optional<BlockToken> securityFault = blockListRepository.findById(refreshToken.refreshToken()); 
        if (securityFault.isPresent()) {
            Users user = usersRepository.findById(securityFault.get().userId()).get();
            AllowToken allowToken = allowListRepository.findByUserId(user.id()).get();

            blockListRepository.save(new BlockToken(allowToken.accessToken(), user.id(), Duration.ofMinutes(15).toSeconds()));
            allowListRepository.delete(allowToken);

            throw new InvalidCredentialsException();
        }

        AllowToken allowToken = allowListRepository.findById(refreshToken.refreshToken()).get();
        allowListRepository.delete(allowToken);

        blockListRepository.save(new BlockToken(refreshToken.refreshToken(), allowToken.userId(), allowToken.expire()));

        UserDetailsImpl userDetails = new UserDetailsImpl(usersRepository.findById(allowToken.userId()).get());

        String newRefreshToken = UUID.randomUUID().toString();
        String newAccessToken = jwtService.generateToken(userDetails);

        allowListRepository.save(new AllowToken(newRefreshToken, newAccessToken, userDetails.getUser().id(), Duration.ofDays(7).toSeconds()));   

        return new TokensDto(newAccessToken, newRefreshToken);
    }

    public String logout(RefreshTokenDto refreshToken) {
        AllowToken allowToken = allowListRepository.findById(refreshToken.refreshToken()).get(); 
        blockListRepository.save(new BlockToken(allowToken.accessToken(), allowToken.userId(), Duration.ofDays(7).toSeconds()));

        allowListRepository.delete(allowToken);
        return usersRepository.findById(allowToken.userId()).get().name();
    }

    public Optional<String> readRefreshToken(HttpServletRequest request){
      return Arrays.stream(request.getCookies())
        .filter(cookie->cookie.getName().equals("refreshToken"))
        .map(Cookie::getValue)
        .findAny();
    }

    public String getUsernameByRequest(HttpServletRequest request) {
        String token = readRefreshToken(request).get();
        AllowToken allowToken = allowListRepository.findById(token).get();
        return usersRepository.findById(allowToken.userId()).get().name();
    }

    public Cookie generateCookie(TokensDto token) {
        Cookie cookie = new Cookie("refreshToken", token.RefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge((int) Duration.ofDays(7).toSeconds());
        return cookie;

    }
}
