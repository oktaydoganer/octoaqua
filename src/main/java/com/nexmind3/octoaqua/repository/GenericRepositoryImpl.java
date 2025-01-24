package com.nexmind3.octoaqua.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.repository.NoRepositoryBean;



@NoRepositoryBean
public class GenericRepositoryImpl<T> extends SimpleJpaRepository<T, Long> implements GenericRepository<T, Number> {

    private final EntityManager entityManager;

    public GenericRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

}
