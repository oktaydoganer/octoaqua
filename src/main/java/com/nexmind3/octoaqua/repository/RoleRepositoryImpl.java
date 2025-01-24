package com.nexmind3.octoaqua.repository;

import com.nexmind3.octoaqua.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Repository;


@Repository
@Primary
public class RoleRepositoryImpl extends GenericRepositoryImpl<Role> implements RoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public RoleRepositoryImpl(EntityManager entityManager) {
        super(JpaEntityInformationSupport.getEntityInformation(Role.class, entityManager), entityManager);
        this.entityManager = entityManager;
    }


}
