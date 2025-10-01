package com.service.AuthRBAC.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.service.AuthRBAC.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.service.AuthRBAC.repository.TokenBlockListRepository;
import com.service.AuthRBAC.repository.UsersRepository;
import com.service.AuthRBAC.model.Users;

import java.io.IOException;
import java.util.Arrays;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService service;

    @Autowired
    private UsersRepository repository;

    @Autowired
    private TokenBlockListRepository blockListRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
        if(endpointNotPublic(request)) {
            String token = service.recoveryToken(request);
            if (token == null) {throw new RuntimeException("absent token");}
            if (blockListRepository.findById(token).isPresent()) {throw new RuntimeException("token in the blacklist");}
            String subject = service.getSubjectFromToken(token);
            Users user = repository.findByName(subject).get();
            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filter.doFilter(request, response);
    }
    
    private boolean endpointNotPublic(HttpServletRequest request) {
        return !Arrays.asList(SecurityConfig.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(request.getRequestURI());
    }
}
