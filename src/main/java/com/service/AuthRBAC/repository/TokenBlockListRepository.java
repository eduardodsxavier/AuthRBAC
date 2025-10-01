
package com.service.AuthRBAC.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.service.AuthRBAC.model.BlockToken;

@Repository
public interface TokenBlockListRepository extends CrudRepository<BlockToken, String> {
}
