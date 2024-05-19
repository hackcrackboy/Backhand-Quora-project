package com.upgrad.quora.api.Controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.Entity.AnsEntity;
import com.upgrad.quora.service.Entity.QuestionEntity;
import com.upgrad.quora.service.Entity.UserAuthTokenEntity;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.dao.Answerdao;
import com.upgrad.quora.service.dao.Dao;
import com.upgrad.quora.service.dao.Questiondao;
import com.upgrad.quora.service.exception.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;


@EnableAutoConfiguration
@RequestMapping("/")
@RestController
@Component
public class AnswerController {
    @Autowired
    private Answerdao answerdao;
    @Autowired
    Questiondao questionDao;
    @Autowired
    Dao userDao;

    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") final String questionId, @RequestBody final AnswerRequest userAnswerRequest, @RequestHeader("Authentication") final String authToken) throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity =   questionDao.getAllQuestions(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }


        if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to post an answer");
        }
        UserEntity user = userAuthTokenEntity.getUser();

        AnsEntity ansEntity = new AnsEntity();
        ansEntity.setUuid(UUID.randomUUID().toString());
        ansEntity.setUser_id(user.getId());
        ansEntity.setId(questionEntity);
        ansEntity.setDate(ZonedDateTime.now());
        ansEntity.setAns(userAnswerRequest.getAnswer());


        final AnsEntity question = answerdao.createAns(ansEntity);

        final AnswerResponse questionResponse = new AnswerResponse().id(question.getUuid()).status("ANSWER CREATED");


        return new ResponseEntity<AnswerResponse>(questionResponse, HttpStatus.CREATED);


    }

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> UpdateAnswer(@PathVariable("answerId") final String answerId, @RequestBody final AnswerEditRequest userAnswerRequest, @RequestHeader("access-token") final String accessToken) throws UserNotFoundException, UnauthorizedException, AuthenticationFailedException, AuthorizationFailedException, InvalidQuestionException, AnswerNotFoundException {


        AnsEntity ansEntity = answerdao.getAllAnswers(answerId);

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to edit the question");
        }
        if (ObjectUtils.notEqual(ansEntity.getUser_id(),userAuthTokenEntity.getUser().getId())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit  an answer");
        }
        if (ansEntity == null) {
            throw new AnswerNotFoundException("‘ANS-001", "Only the answer owner can edit the answer");
        }


        ansEntity.setAns(userAnswerRequest.getContent());
        answerdao.UpdateAns(ansEntity);

        final AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(ansEntity.getUuid()).status("ANSWER EDITED");


        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> Deleteanswer(@PathVariable("answerId") final String answerId, @RequestHeader("access-token") final String accessToken) throws UserNotFoundException, UnauthorizedException, AuthenticationFailedException, AuthorizationFailedException, InvalidQuestionException, AnswerNotFoundException, SignUpRestrictedException {


        AnsEntity questionEntity = answerdao.getAllAnswers(answerId);

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

        UserEntity userEntity = userDao.getUser(String.valueOf(userAuthTokenEntity.getUser()));
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to delete the question");
        }
        if (ObjectUtils.notEqual(questionEntity.getUser_id(), userAuthTokenEntity.getUser())&& userEntity.getRole().equalsIgnoreCase("nonadmin")) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }

        if (questionEntity == null) {
            throw new AnswerNotFoundException("‘ANS-001", "Only the answer owner can edit the answer");
        }
        answerdao.DeleteAns(questionEntity);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(questionEntity.getUuid()).status("‘QUESTION DELETED");


        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>>AllAnswer(@PathVariable("questionId") final String questionId, @RequestHeader("access-token") final String accessToken) throws UserNotFoundException, UnauthorizedException, AuthenticationFailedException, AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity = questionDao.getAllQuestions(questionId);
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to get all questions");
        }
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        List<AnsEntity> Answers = new ArrayList<>(Collections.emptyList());


        for (int id = 1; id != Answers.size(); id++) {

            List<AnsEntity> ansEntityList = answerdao.getuserAnswer(id);

            for (int i = 0; i < ansEntityList.toArray().length; i++) {

                if (!Objects.equals(ansEntityList.get(i).getId(),questionEntity))
                    throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
                else {
                    Answers.add(ansEntityList.get(i));
                }
            }
        }



        final LinkedHashMap<String, String> answerDetailsList = getAnswerDetailsResponses(Answers);

        List<AnswerDetailsResponse> answerDetailsResponses1 = new ArrayList<>(Collections.singletonList(new AnswerDetailsResponse()));
        // Create a response object and set the question details list

        // Return the response entity with the response object and HTTP status code 200 (OK)
        for (Map.Entry m : answerDetailsList.entrySet()) {
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();


            answerDetailsResponse.answerContent((String) m.getValue()).id((String) m.getKey()).questionContent(questionEntity.getContent());
            answerDetailsResponses1.add(answerDetailsResponse);
        }
        answerDetailsResponses1.remove(0);

        // Return the response entity with the response object and HTTP status code 200 (OK)
        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponses1, HttpStatus.OK);


    }

    private static LinkedHashMap<String, String> getAnswerDetailsResponses(List<AnsEntity> Answers) {
        List<AnswerDetailsResponse> answerDetailsList1 = new ArrayList<>(Collections.emptyList());

        // Iterate over each question to create QuestionDetails objects
        for (AnsEntity answer :Answers) {
            // Create a QuestionDetails object for each question
           AnswerDetailsResponse questionDetails = new AnswerDetailsResponse();
            questionDetails.id(String.valueOf(answer.getUuid()));
            questionDetails.setAnswerContent(answer.getAns());

            // Add the QuestionDetails object to the list
          answerDetailsList1.add(questionDetails);
        }
        LinkedHashMap<String,String>map = new LinkedHashMap<>();
        String key,value;
        for (AnswerDetailsResponse answerDetailsResponse :answerDetailsList1) {
            key = answerDetailsResponse.getId();
            value = answerDetailsResponse.getAnswerContent();
            map.put(key, value);

        }

        return  map;

    }
}




