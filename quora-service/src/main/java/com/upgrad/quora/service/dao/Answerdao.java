package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.Entity.AnsEntity;
import com.upgrad.quora.service.Entity.QuestionEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class Answerdao {

        @PersistenceContext
        private EntityManager entityManager1;


    @Transactional(propagation = Propagation.REQUIRED)
        public AnsEntity createAns(AnsEntity ansEntity) {
            entityManager1.persist(ansEntity);
            return ansEntity;
        }


    @Transactional(propagation = Propagation.REQUIRED)
        public AnsEntity getAllAnswers(final String id) {
            try {


                return entityManager1.createNamedQuery("answerByUuid",AnsEntity.class).setParameter("id", id)
                        .getSingleResult();

            } catch (NoResultException nre) {
                return new AnsEntity();
            }
        }

    @Transactional(propagation = Propagation.REQUIRED)
        public void UpdateAns(final AnsEntity question) {
            entityManager1.merge(question);
        }

        public void DeleteUser(QuestionEntity userEntity) {
            entityManager1.remove(userEntity);

        }

    @Transactional(propagation = Propagation.REQUIRED)
        public List<
            AnsEntity> getuserAnswer(final Integer userid) {
            try {


                return entityManager1.createNamedQuery("answerByUserId", AnsEntity.class).setParameter("user_id", userid)
                        .getResultList();

            } catch (NoResultException nre) {
                return null;
            }
        }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnsEntity DeleteAns(AnsEntity userEntity){
        entityManager1.remove(userEntity);
        return userEntity;
    }
    }



