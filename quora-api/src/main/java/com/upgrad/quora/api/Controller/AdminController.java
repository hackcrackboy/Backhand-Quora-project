package com.upgrad.quora.api.Controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.dao.Dao;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@EnableAutoConfiguration
@RequestMapping("/admin/")
@RestController
@Component
public class AdminController {
    @Autowired
    private UserBusinessService userBusinessService;

   @Autowired
    private Dao userdao;

    @RequestMapping(method = RequestMethod.GET, path = "users/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> DeleteUser(@PathVariable("id") final String userUuid, @RequestHeader("authenticate") final String accessToken) throws UserNotFoundException, AuthenticationFailedException, AuthorizationFailedException, SignUpRestrictedException {
        final UserEntity userEntity = userBusinessService.DeleteUser(userUuid, accessToken);
UserDeleteResponse userDetailsResponse = new UserDeleteResponse().id(userEntity.getUuid()).status("USER SUCCESSFULLY DELETED");

        return new ResponseEntity<UserDeleteResponse>(userDetailsResponse, HttpStatus.OK);
    }
}

