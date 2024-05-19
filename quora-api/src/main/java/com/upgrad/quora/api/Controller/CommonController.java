package com.upgrad.quora.api.Controller;

import com.upgrad.quora.api.model.AuthorizedUserResponse;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.business.CommonBuisness;

import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UnauthorizedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@EnableAutoConfiguration
@RequestMapping("/userprofiles/")
@RestController
@Component
public class CommonController {


    @Autowired
    private CommonBuisness commonBuisness;
    @RequestMapping(method = RequestMethod.GET, path = "/{Id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AuthorizedUserResponse>getUserid(@PathVariable("Id") final String userUuid , @RequestHeader("authorization") final String authorization) throws UserNotFoundException, UnauthorizedException, AuthorizationFailedException, SignUpRestrictedException {
        final UserEntity userEntity = commonBuisness.getProfile(userUuid,authorization);
        AuthorizedUserResponse userDetailsResponse = new AuthorizedUserResponse();
        userDetailsResponse.firstName(userEntity.getFirstName());
        userDetailsResponse.id(UUID.fromString(userEntity.getUuid()));
        userDetailsResponse.lastName(userEntity.getLastName());
        userDetailsResponse.emailAddress(userEntity.getEmail());
        userDetailsResponse.mobilePhone(userEntity.getContactNumber());
        return new ResponseEntity<AuthorizedUserResponse>(userDetailsResponse,HttpStatus.OK);
    }}