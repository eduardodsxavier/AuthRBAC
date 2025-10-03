package com.service.AuthRBAC.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

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
import com.service.AuthRBAC.model.AllowToken;
import com.service.AuthRBAC.model.BlockToken;
import com.service.AuthRBAC.model.Log;
import com.service.AuthRBAC.enums.Role;
import com.service.AuthRBAC.exception.InvalidCredentialsException;
import com.service.AuthRBAC.repository.LogRepository;
import com.service.AuthRBAC.repository.TokenAllowListRepository;
import com.service.AuthRBAC.repository.TokenBlockListRepository;
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
    private LogRepository logRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenAllowListRepository allowListRepository;

    @Autowired
    private TokenBlockListRepository blockListRepository;

    @Autowired
    private JwtService jwtService;

    public TokenDto authenticate(LoginDto loginInfo) {
        UsernamePasswordAuthenticationToken userAuthenticationToken = new UsernamePasswordAuthenticationToken(loginInfo.username(), loginInfo.password());

        Authentication authentication = manager.authenticate(userAuthenticationToken); 

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String refreshToken = UUID.randomUUID().toString();
        String accessToken = jwtService.generateToken(userDetails);

        allowListRepository.save(new AllowToken(refreshToken, accessToken, userDetails.getId(), Duration.ofDays(7).toSeconds()));

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

        return new TokenDto(newAccessToken, newRefreshToken);
    }

    public void logout(RefreshTokenDto refreshToken) {
        AllowToken allowToken = allowListRepository.findById(refreshToken.refreshToken()).get(); 
        blockListRepository.save(new BlockToken(allowToken.accessToken(), allowToken.userId(), Duration.ofDays(7).toSeconds()));

        allowListRepository.delete(allowToken);
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

    public List<Log> auditLogs() {
        return logRepository.findAll();
    }

    public Optional<String> readServletCookie(HttpServletRequest request){
      return Arrays.stream(request.getCookies())
        .filter(cookie->cookie.getName().equals("refreshToken"))
        .map(Cookie::getValue)
        .findAny();
    }
}
