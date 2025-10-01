package com.service.AuthRBAC.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.service.AuthRBAC.model.AllowToken;

@Repository
public interface TokenAllowListRepository extends CrudRepository<AllowToken, String> {

    Optional<AllowToken> findByUserId(Long userId);
}
