package com.nexmind3.octoaqua.repository;

import com.nexmind3.octoaqua.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends GenericRepository<User, Number>{
    User findByEmail(String email);

    Long getUserIdByMail(String email) ;

    List<Long> getCompanyIdByUserMail(String email) ;
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findUsersByRoleName(@Param("roleName") String roleName);
}
