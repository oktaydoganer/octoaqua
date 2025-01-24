package com.nexmind3.octoaqua.repository;

import com.nexmind3.octoaqua.entity.Company;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Repository;


@Repository
@Primary
public class CompanyRepositoryImpl extends GenericRepositoryImpl<Company> implements CompanyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public CompanyRepositoryImpl(EntityManager entityManager) {
        super(JpaEntityInformationSupport.getEntityInformation(Company.class, entityManager), entityManager);
        this.entityManager = entityManager;
    }


}
