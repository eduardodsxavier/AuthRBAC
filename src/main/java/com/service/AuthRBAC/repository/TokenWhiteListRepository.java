package com.service.AuthRBAC.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.service.AuthRBAC.model.TokenWhiteList;

@Repository
public interface TokenWhiteListRepository extends CrudRepository<TokenWhiteList, String> {

    Optional<TokenWhiteList> findByUserId(Long userId);
}
