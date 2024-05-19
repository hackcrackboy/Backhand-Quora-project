package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.Entity.UserAuthTokenEntity;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.hibernate.loader.custom.Return;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import static java.lang.System.err;
import static java.sql.JDBCType.NULL;

@Repository
public class Dao {

    @PersistenceContext
    private EntityManager entityManager;
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity Signup(UserEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getuserid(final String userUuid) throws SignUpRestrictedException {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid)
                    .getSingleResult();

        } catch (NoResultException nre) {
           return null;
        } }
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity DeleteUser(UserEntity userEntity){
        entityManager.remove(userEntity);
        return userEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity){
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity getUserAuthToken(final String accessToken){
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken",
                    UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre){
            return new UserAuthTokenEntity();
        }

    } @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getUserByUsername(final String username) throws SignUpRestrictedException {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return new UserEntity();
        }
    }
        @Transactional(propagation = Propagation.REQUIRED)
        public UserEntity getUser(final String userUuid) throws SignUpRestrictedException {
            try {
                return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid)
                        .getSingleResult();

            } catch (NoResultException nre) {
                return new UserEntity();
            } }
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAuth(final UserAuthTokenEntity updatedUserEntity){
        entityManager.merge(updatedUserEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(final UserEntity updatedUserEntity){
        entityManager.merge(updatedUserEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getUserByEmail(final String email)  {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return new UserEntity();
        }

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity DeleteUser(UserAuthTokenEntity userEntity){
        entityManager.remove(userEntity);
        return userEntity;
    }}


