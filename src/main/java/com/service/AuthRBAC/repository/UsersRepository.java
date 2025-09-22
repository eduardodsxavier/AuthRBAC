package com.service.AuthRBAC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.AuthRBAC.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long>  {
    
}

