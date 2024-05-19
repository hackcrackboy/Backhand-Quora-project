package com.upgrad.quora.service.business;

import com.upgrad.quora.service.Entity.UserAuthTokenEntity;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.dao.Dao;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Objects;

@Service
public class CommonBuisness {
@Autowired
    private  Dao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getProfile(final String userUuid, final String authorizationToken) throws UserNotFoundException, UnauthorizedException, AuthorizationFailedException, SignUpRestrictedException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);
       ZonedDateTime now = ZonedDateTime.now();
        UserEntity userEntity = userDao.getUser(userUuid);
        if( userAuthTokenEntity == null ) {
            /*  UserEntity userEntity = userDao.getUser(userUuid);*/
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
            if(userAuthTokenEntity.getLogoutAt().isBefore(now)){
        throw new AuthorizationFailedException("ATHR-002","User is signed out. Sign in first to get user details");

        }
            if(userEntity ==null){
                throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
            }
        return userEntity;
    }
}
