package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.Entity.QuestionEntity;
import com.upgrad.quora.service.Entity.UserAuthTokenEntity;
import com.upgrad.quora.service.Entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;




@Repository
public class Questiondao {
    @PersistenceContext
    private EntityManager entityManager;


    public QuestionEntity createQues(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }


    public QuestionEntity getAllQuestions(final String id) {
        try {


            return entityManager.createNamedQuery("questionById", QuestionEntity.class).setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }

    public void UpdateQues(final QuestionEntity question) {
        entityManager.merge(question);
    }

    public void DeleteUser(QuestionEntity userEntity) {
        entityManager.remove(userEntity);

    }
    public QuestionEntity DeleteQues(QuestionEntity userEntity){
        entityManager.remove(userEntity);
        return userEntity;
    }

    public QuestionEntity getuserques(final String userid) {
        try {


            return entityManager.createNamedQuery("questionByUserId", QuestionEntity.class).setParameter("user_id", userid)
                    .getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }
}



