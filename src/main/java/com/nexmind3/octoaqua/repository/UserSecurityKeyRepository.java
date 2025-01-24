package com.nexmind3.octoaqua.repository;

import com.nexmind3.octoaqua.entity.UserSecurityKey;
import com.nexmind3.octoaqua.enumeration.SecurityKeyType;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSecurityKeyRepository extends GenericRepository<UserSecurityKey, Number> {
     UserSecurityKey getActiveSecurityKey(Long userId, SecurityKeyType type, String key);
}
