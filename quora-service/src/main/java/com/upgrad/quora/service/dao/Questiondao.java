package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.Entity.QuestionEntity;
import com.upgrad.quora.service.Entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class Questiondao {
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQues(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity getAllQuestions(final String id) {
        try {


            return entityManager.createNamedQuery("questionById", QuestionEntity.class).setParameter("uuid", id)
                    .getSingleResult();

        } catch (NoResultException nre) {
           return new QuestionEntity();
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void UpdateQues(final QuestionEntity question) {
        entityManager.merge(question);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void DeleteUser(QuestionEntity userEntity) {
        entityManager.remove(userEntity);

    } @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity DeleteQues(QuestionEntity userEntity){
        entityManager.remove(userEntity);
        return userEntity;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity>getallUserQuestions(final Integer userId) {
        try {
            return entityManager.createNamedQuery("questionByUserId", QuestionEntity.class)
                    .setParameter("user_id", userId)
                    .getResultList();

        } catch (NoResultException nre) {
           return null;
        }
    }
}



