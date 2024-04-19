package com.upgrad.quora.api.Controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.dao.Dao;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UnauthorizedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class AdminController {

    private UserBusinessService userBusinessService;


    private Dao userdao;

    @RequestMapping(method = RequestMethod.GET, path = "admin/users/{userid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> DeleteUser(@PathVariable("userid") final String userUuid, @RequestHeader("authenticate") final String accessToken) throws UserNotFoundException, UnauthorizedException, AuthenticationFailedException, AuthorizationFailedException {
        final UserEntity userEntity = userBusinessService.DeleteUser(userUuid, accessToken);
UserDeleteResponse userDetailsResponse = new UserDeleteResponse().id(userEntity.getUuid()).status("USER SUCCESSFULLY DELETED");
      userdao.DeleteUser(userEntity);
        return new ResponseEntity<UserDeleteResponse>(userDetailsResponse, HttpStatus.OK);
    }
}

