
package com.service.AuthRBAC.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.service.AuthRBAC.model.TokenBlackList;

@Repository
public interface TokenBlackListRepository extends CrudRepository<TokenBlackList, String> {
}
