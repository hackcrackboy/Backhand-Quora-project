package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.Entity.QuestionEntity;
import com.upgrad.quora.service.Entity.UserAuthTokenEntity;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class Dao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity Signup(UserEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;

    }
    public UserEntity getuserid(final String userUuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid)
                    .getSingleResult();

        } catch (NoResultException nre) {
            return null;
        } }
    public UserEntity DeleteUser(UserEntity userEntity){
        entityManager.remove(userEntity);
        return userEntity;
    }


    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity){
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;

    }
    public UserAuthTokenEntity getUserAuthToken(final String accessToken){
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken",
                    UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }

    } public UserEntity getUserByUsername(final String username) {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }
    public void updateAuth(final UserAuthTokenEntity updatedUserEntity){
        entityManager.merge(updatedUserEntity);
    }


    public void updateUser(final UserEntity updatedUserEntity){
        entityManager.merge(updatedUserEntity);
    }

    public UserEntity getUser(final String userUuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid)
                    .getSingleResult();

        } catch (NoResultException nre) {
            return null;
        } }
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }}


