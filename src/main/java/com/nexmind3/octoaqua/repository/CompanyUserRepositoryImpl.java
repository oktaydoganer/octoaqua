package com.nexmind3.octoaqua.repository;

import com.nexmind3.octoaqua.entity.CompanyUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class CompanyUserRepositoryImpl extends GenericRepositoryImpl<CompanyUser> implements CompanyUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public CompanyUserRepositoryImpl(EntityManager entityManager) {
        super(JpaEntityInformationSupport.getEntityInformation(CompanyUser.class, entityManager), entityManager);
        this.entityManager = entityManager;
    }

}
