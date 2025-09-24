package com.service.AuthRBAC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.service.AuthRBAC.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long>  {

    Optional<Users> findByName(String name);
    
}

