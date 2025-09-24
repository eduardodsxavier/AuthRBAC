package com.service.AuthRBAC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.service.AuthRBAC.repository.UsersRepository;
import com.service.AuthRBAC.security.UserDetailsImpl;
import com.service.AuthRBAC.model.Users;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repository.findByName(username).orElseThrow(() -> new RuntimeException("User not find"));
        return new UserDetailsImpl(user);
    }
    
}
