package com.nexmind3.octoaqua.repository;

import com.nexmind3.octoaqua.entity.UserSecurityKey;
import com.nexmind3.octoaqua.enumeration.SecurityKeyType;
import com.nexmind3.octoaqua.enumeration.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Repository;


@Repository
@Primary
public class UserSecurityKeyRepositoryImpl extends GenericRepositoryImpl<UserSecurityKey> implements UserSecurityKeyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UserSecurityKeyRepositoryImpl(EntityManager entityManager) {
        super(JpaEntityInformationSupport.getEntityInformation(UserSecurityKey.class, entityManager), entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public UserSecurityKey getActiveSecurityKey(Long userId, SecurityKeyType type, String key) {
        //liste çevir
        UserSecurityKey userSecurityKey = null ;
        if(userId == null || type == null){
            return userSecurityKey;
        }

        StringBuilder queryString = new StringBuilder("select usk from UserSecurityKey usk ")
                .append(" join User u on u.id = usk.user.id ")
                .append(" WHERE u.id = :userId and usk.securityKeyType = :type and usk.state = :active and usk.expirationTime > CURRENT_TIMESTAMP ");

        if(StringUtils.isNotBlank(key)){
            queryString.append(" and usk.key = :key");
        }

        TypedQuery<UserSecurityKey> query = entityManager.createQuery( queryString.toString(), UserSecurityKey.class);

        query.setParameter("userId", userId);
        query.setParameter("type", type);
        query.setParameter("active", State.ACTIVE);
        if(StringUtils.isNotBlank(key)){
            query.setParameter("key", key);
        }
        try {
             userSecurityKey = query.getSingleResult();
        } finally {
           return userSecurityKey;
        }
    }

}
