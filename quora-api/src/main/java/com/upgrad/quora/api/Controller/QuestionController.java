package com.upgrad.quora.api.Controller;
import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.Entity.QuestionEntity;
import com.upgrad.quora.service.Entity.UserAuthTokenEntity;
import com.upgrad.quora.service.Entity.UserEntity;
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
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;


@EnableAutoConfiguration
@RequestMapping("/")
@RestController
@Component
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

Date date =new Date();
        UserEntity user = userAuthTokenEntity.getUser();
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setDate(ZonedDateTime.from(ZonedDateTime.now()));
        questionEntity.setUser_id(user.getId());

        questionEntity.setContent(userquestionRequest.getContent());

        final QuestionEntity question = questionDao.createQues(questionEntity);

        final QuestionResponse questionResponse = new QuestionResponse().id(question.getUuid()).status("QUESTION CREATED");


        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);


    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>>  Allquestion(@RequestHeader("access-token") final String accessToken) throws AuthenticationFailedException, AuthorizationFailedException {


        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to get all questions");
        }

        List<QuestionEntity> Questions = new ArrayList<>(Collections.emptyList());

        for (int id = 1; id!=Questions.size(); id++) {

            List<QuestionEntity> questionEntity = questionDao.getallUserQuestions(id);


            for (int i = 0; i < questionEntity.toArray().length; i++) {

                Questions.add(questionEntity.get(i));
            }
        }




            final LinkedHashMap<String, String> questionDetailsList = getQuestionDetailsResponses(Questions);


        List<QuestionDetailsResponse> questionDetailsResponse1 = new ArrayList<>(Collections.singletonList(new QuestionDetailsResponse()));
            // Create a response object and set the question details list


        // Return the response entity with the response object and HTTP status code 200 (OK)
        for(Map.Entry m:questionDetailsList.entrySet()) {
            QuestionDetailsResponse questionDetails = new QuestionDetailsResponse();

             questionDetails.content((String) m.getValue()).id((String) m.getKey());
            questionDetailsResponse1.add(questionDetails);
        }
        questionDetailsResponse1.remove(0);

            // Return the response entity with the response object and HTTP status code 200 (OK)
            return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponse1, HttpStatus.OK);

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
        if (ObjectUtils.notEqual(questionEntity.getUser_id(), userAuthTokenEntity.getUser())) {
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
public ResponseEntity<QuestionDeleteResponse> Deletequestion(@PathVariable("questionId") final String questionId, @RequestHeader("access-token") final String accessToken) throws UserNotFoundException, UnauthorizedException, AuthenticationFailedException, AuthorizationFailedException, InvalidQuestionException, SignUpRestrictedException {


    QuestionEntity questionEntity = questionDao.getAllQuestions(questionId);

    UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

    UserEntity userEntity = userDao.getUser(String.valueOf(userAuthTokenEntity.getUser()));
    if (userAuthTokenEntity == null) {
        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }
    if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
        throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to delete the question");
    }
    if (Objects.equals(questionEntity.getUser_id(),userAuthTokenEntity.getUser())&& userEntity.getRole().equalsIgnoreCase("nonadmin")) {
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
public ResponseEntity<List<QuestionDetailsResponse>> AllquestionByuser(@PathVariable("userId") final Integer questionId,  @RequestHeader("access-token") final String accessToken) throws UserNotFoundException, UnauthorizedException, AuthenticationFailedException, AuthorizationFailedException, InvalidQuestionException, SignUpRestrictedException {




    UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
    UserEntity userEntity = userDao.getUser(userAuthTokenEntity.getUuid());
    if (userAuthTokenEntity == null) {
        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }
    if (ZonedDateTime.now().isAfter(userAuthTokenEntity.getLogoutAt())) {
        throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to get all questions posted by a specific user");
    }

    if (userEntity == null) {
        throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
    }



    List<QuestionEntity> Questions = new ArrayList<>(Collections.emptyList());




    List<QuestionEntity> questionEntity = questionDao.getallUserQuestions(questionId);

      for(int i = 0;i<questionEntity.toArray().length; i++) {

              Questions.add(questionEntity.get(i));
      }

    final LinkedHashMap<String, String> questionDetailsList = getQuestionDetailsResponses(Questions);


    List<QuestionDetailsResponse> questionDetailsResponse1 = new ArrayList<>(Collections.singletonList(new QuestionDetailsResponse()));
    // Create a response object and set the question details list

    QuestionDetailsResponse questionDetails = new QuestionDetailsResponse();
    // Return the response entity with the response object and HTTP status code 200 (OK)
    for(Map.Entry m:questionDetailsList.entrySet()) {



        questionDetails.content((String) m.getValue()).id((String) m.getKey());
        questionDetailsResponse1.add(questionDetails);
    }
    questionDetailsResponse1.remove(0);





    return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponse1, HttpStatus.OK);





}

    private static LinkedHashMap<String, String> getQuestionDetailsResponses(List<QuestionEntity> Questions) {
        List<QuestionDetailsResponse> questionDetailsList1 = new ArrayList<>(Collections.emptyList());

        // Iterate over each question to create QuestionDetails objects
        for (QuestionEntity question : Questions) {
            // Create a QuestionDetails object for each question
            QuestionDetailsResponse questionDetails = new QuestionDetailsResponse();
            questionDetails.id(String.valueOf(question.getUuid()));
            questionDetails.setContent(question.getContent());

            // Add the QuestionDetails object to the list
            questionDetailsList1.add(questionDetails);
        }
        LinkedHashMap<String,String>map = new LinkedHashMap<>();
        String key,value;
        for (QuestionDetailsResponse questionDetailsResponse : questionDetailsList1) {
            key = questionDetailsResponse.getId();
            value = questionDetailsResponse.getContent();
            map.put(key, value);

        }

        return  map;

    }
}






