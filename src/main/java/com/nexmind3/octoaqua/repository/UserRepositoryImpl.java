package com.nexmind3.octoaqua.repository;

import com.nexmind3.octoaqua.entity.User;
import com.nexmind3.octoaqua.enumeration.CompanyUserType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
@Primary
public class UserRepositoryImpl extends GenericRepositoryImpl<User> implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        super(JpaEntityInformationSupport.getEntityInformation(User.class, entityManager), entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public User findByEmail(String email) {
        User user = null;
        TypedQuery<User> query = entityManager.createQuery(
                "select e from User e WHERE e.email = :email", User.class);
        try {
            user = query.setParameter("email", email).getSingleResult();
        } finally {
            return user;
        }
    }

    @Override
    public Long getUserIdByMail(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        }
        StringBuilder sql = new StringBuilder("SELECT au.id ")
                .append("FROM app_user au ")
                .append("WHERE au.email = :email ");

        Query query = entityManager.createNativeQuery(sql.toString()).unwrap(NativeQuery.class);

        Long userId = (Long) query.setParameter("email", email).uniqueResult();
        return userId;
    }

    @Override
    public List<Long> getCompanyIdByUserMail(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        }
        StringBuilder sql = new StringBuilder("select cu.company from company_user cu  where ")
                .append("exists(select 1 from app_user au where cu.user_id = au.id and au.email = :email) and ")
                .append("exists(select 1 from company c where c.id = cu.company) and cu.\"type\" in (:userTypes) group by cu.company ");

        Query query = entityManager.createNativeQuery(sql.toString()).unwrap(NativeQuery.class);

        List<Long> userCompanyIds = query.setParameter("email", email).setParameter("userTypes", Arrays.asList(CompanyUserType.ADMIN.ordinal(), CompanyUserType.OWNER.ordinal())).list();
        return userCompanyIds;
    }

    @Override
    public List<User> findUsersByRoleName(String roleName) {
        return null;
    }

}