package com.upgrad.quora.api.Controller;

import com.upgrad.quora.api.model.AuthorizedUserResponse;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.business.CommonBuisness;

import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UnauthorizedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequestMapping("/")
@RestController
public class CommonController {

    private CommonBuisness commonBuisness;
    @RequestMapping(method = RequestMethod.GET, path = "/userprofiles/{userid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AuthorizedUserResponse> getUserid(@PathVariable("userid") final String userUuid , @RequestHeader("authorization") final String authorization) throws UserNotFoundException, UnauthorizedException, AuthorizationFailedException {
        final UserEntity userEntity = commonBuisness.getProfile(userUuid,authorization);
        AuthorizedUserResponse userDetailsResponse = new AuthorizedUserResponse();
        userDetailsResponse.firstName(userEntity.getFirstName());
        userDetailsResponse.id(UUID.fromString(userEntity.getUuid()));
        userDetailsResponse.lastName(userEntity.getLastName());
        userDetailsResponse.emailAddress(userEntity.getEmail());
        userDetailsResponse.mobilePhone(userEntity.getContactNumber());
        return new ResponseEntity<AuthorizedUserResponse>(userDetailsResponse, HttpStatus.OK);
    }}