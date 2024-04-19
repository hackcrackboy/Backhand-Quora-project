package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.Entity.AnsEntity;
import com.upgrad.quora.service.Entity.QuestionEntity;
import com.upgrad.quora.service.Entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Repository
public class Answerdao {

        @PersistenceContext
        private EntityManager entityManager;


        public AnsEntity createAns(AnsEntity ansEntity) {
            entityManager.persist(ansEntity);
            return ansEntity;
        }


        public AnsEntity getAllAnswers(final String id) {
            try {


                return entityManager.createNamedQuery("answerById",AnsEntity.class).setParameter("id", id)
                        .getSingleResult();

            } catch (NoResultException nre) {
                return null;
            }
        }

        public void UpdateAns(final AnsEntity question) {
            entityManager.merge(question);
        }

        public void DeleteUser(QuestionEntity userEntity) {
            entityManager.remove(userEntity);

        }

        public AnsEntity getuuserAnswer(final String userid) {
            try {


                return entityManager.createNamedQuery("answerByUserId", AnsEntity.class).setParameter("user_id", userid)
                        .getSingleResult();

            } catch (NoResultException nre) {
                return null;
            }
        }
    public AnsEntity DeleteAns(AnsEntity userEntity){
        entityManager.remove(userEntity);
        return userEntity;
    }
    }



