package com.upgrad.quora.api.Controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.Entity.QuestionEntity;
import com.upgrad.quora.service.Entity.UserAuthTokenEntity;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.business.UserAuthbuisness;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.dao.Dao;
import com.upgrad.quora.service.dao.Questiondao;
import com.upgrad.quora.service.exception.*;
import lombok.val;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import java.util.UUID;

public class QuestionController {
    @Autowired
   Questiondao questionDao;
    @Autowired
    Dao userDao;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> questionscreate(@RequestBody final QuestionRequest userquestionRequest, @RequestHeader("authenticate") final String authToken) throws AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to post a question");
        }


        UserEntity user = userAuthTokenEntity.getUser();
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUser(user);
        questionEntity.setContent(userquestionRequest.getContent());


        final QuestionEntity question = questionDao.createQues(questionEntity);

        final QuestionResponse questionResponse = new QuestionResponse().id(question.getUuid()).status("QUESTION CREATED");


        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);


    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> Allquestion(@RequestHeader("access-token") final String accessToken) throws UserNotFoundException, UnauthorizedException, AuthenticationFailedException, AuthorizationFailedException {


        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to get all questions");
        }

        ArrayList<QuestionEntity> Questions = new ArrayList<QuestionEntity>();

        for (int id = 1; id != 0; id++) {

            QuestionEntity questionEntity = questionDao.getAllQuestions(String.valueOf(id));
            if (questionEntity != null) {
                Questions.add(questionEntity);
            } else {
                id = 0;
                break;
            }
        }


        ArrayList<QuestionDetailsResponse> questionDetailsList = new ArrayList<>();

        // Iterate over each question to create QuestionDetails objects
        for (QuestionEntity question : Questions) {
            // Create a QuestionDetails object for each question
            QuestionDetailsResponse questionDetails = new QuestionDetailsResponse();
            questionDetails.setId(question.getUuid());
            questionDetails.setContent(question.getContent());

            // Add the QuestionDetails object to the list
            questionDetailsList.add(questionDetails);
        }

        // Create a response object and set the question details list
        final QuestionDetailsResponse response = new QuestionDetailsResponse().id(String.valueOf(questionDetailsList)).content(String.valueOf(questionDetailsList));

        // Return the response entity with the response object and HTTP status code 200 (OK)
        return new ResponseEntity<QuestionDetailsResponse>((MultiValueMap<String, String>) questionDetailsList, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> Updatequestion(@PathVariable("questionId") final String questionId, @RequestBody final QuestionRequest userquestionRequest, @RequestHeader("access-token") final String accessToken) throws UserNotFoundException, UnauthorizedException, AuthenticationFailedException, AuthorizationFailedException, InvalidQuestionException {


        QuestionEntity questionEntity = questionDao.getAllQuestions(questionId);

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to edit the question");
        }
        if (questionEntity.getUser() != userAuthTokenEntity.getUser()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }


        questionEntity.setContent(userquestionRequest.getContent());
        questionDao.UpdateQues(questionEntity);

        final QuestionEditResponse questionResponse = new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");


        return new ResponseEntity<QuestionEditResponse>(questionResponse, HttpStatus.OK);


    }

@RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<QuestionDeleteResponse> Deletequestion(@PathVariable("questionId") final String questionId, @RequestHeader("access-token") final String accessToken) throws UserNotFoundException, UnauthorizedException, AuthenticationFailedException, AuthorizationFailedException, InvalidQuestionException {


    QuestionEntity questionEntity = questionDao.getAllQuestions(questionId);

    UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

    UserEntity userEntity = userDao.getUser(String.valueOf(userAuthTokenEntity.getUser()));
    if (userAuthTokenEntity == null) {
        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }
    if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
        throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to delete the question");
    }
    if (questionEntity.getUser() != userAuthTokenEntity.getUser() && userEntity.getRole().equalsIgnoreCase("nonadmin")) {
        throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
    }
    if (questionEntity == null) {
        throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
    }
    questionDao.DeleteQues(questionEntity);
QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid()).status("‘QUESTION DELETED");


    return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);



}
@RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<QuestionDetailsResponse> AllquestionByuser(@PathVariable("userId") final String questionId,  @RequestHeader("access-token") final String accessToken) throws UserNotFoundException, UnauthorizedException, AuthenticationFailedException, AuthorizationFailedException, InvalidQuestionException {


    UserEntity userEntity = userDao.getUser(questionId);

    UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
    if (userAuthTokenEntity == null) {
        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }
    if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
        throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to get all questions posted by a specific user");
    }

    if (userEntity == null) {
        throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
    }


    ArrayList<QuestionEntity> Questions = new ArrayList<QuestionEntity>();

    int i = 0;
    while (i <= 10) {
        QuestionEntity questionEntity = questionDao.getuserques(questionId);
        if (questionEntity != null) {
            Questions.add(questionEntity);
        } else {
            i = i + 11;
            break;
        }
    }


    ArrayList<QuestionDetailsResponse> questionDetailsList1 = new ArrayList<>();

    // Iterate over each question to create QuestionDetails objects
    for (QuestionEntity question : Questions) {
        // Create a QuestionDetails object for each question
        QuestionDetailsResponse questionDetails = new QuestionDetailsResponse();
        questionDetails.setId(question.getUuid());
        questionDetails.setContent(question.getContent());

        // Add the QuestionDetails object to the list
        questionDetailsList1.add(questionDetails);
    }


    // Return the response entity with the response object and HTTP status code 200 (OK)
    return new ResponseEntity<QuestionDetailsResponse>((MultiValueMap<String, String>) questionDetailsList1, HttpStatus.OK);

}
}




