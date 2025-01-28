package com.nexmind3.octoaqua.repository;

import com.nexmind3.octoaqua.entity.Relay;
import com.nexmind3.octoaqua.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Primary
public class RelayRepositoryImpl extends GenericRepositoryImpl<Relay> implements RelayRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public RelayRepositoryImpl(EntityManager entityManager) {
        super(JpaEntityInformationSupport.getEntityInformation(Relay.class, entityManager), entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<Relay> getAllSorted() {
        return (List<Relay>)entityManager.createQuery("from Relay order by id").getResultList();
    }

    @Override
    public Relay findByName(String xName) {
        Query query = entityManager.createQuery("from Relay where xName=:xName order by id");
        query.setParameter("xName", xName);
        return (Relay) query.getSingleResult();
    }

}
