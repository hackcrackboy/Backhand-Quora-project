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

import static java.lang.Object.*;


@Service
    public class UserBusinessService {

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;
    @Autowired
    private Dao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
        public UserEntity Signup(UserEntity userEntity) throws SignUpRestrictedException {


            UserEntity user5 = userDao.getUserByEmail(userEntity.getEmail());

           UserEntity user = userDao.getUserByUsername(userEntity.getUserName());

            if (Objects.equals(user.getUserName(),userEntity.getUserName())) {
                 throw new SignUpRestrictedException("SGR-001", "Try any other Username,this Username has already been taken");
            }
            else if(Objects.equals(user5.getEmail(), userEntity.getEmail())) {
                throw new SignUpRestrictedException("SGR-002", "This user has already been registered,try with any other emailId");
            }
   else {
         return userEntity;
     }
}
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity DeleteUser(final String userUuid, final String authToken) throws UserNotFoundException, AuthorizationFailedException, AuthenticationFailedException, SignUpRestrictedException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authToken);
        ZonedDateTime now = ZonedDateTime.now();
        UserEntity userEntity = userDao.getUser(userUuid);
        if( userAuthTokenEntity == null ) {
            /*  UserEntity userEntity = userDao.getUser(userUuid);*/
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if(userAuthTokenEntity.getLogoutAt().isBefore(now)){
            throw new AuthorizationFailedException("ATHR-002","User is signed out");

        }
        String role1 = "nonadmin";
        if(Objects.equals(role1, userEntity.getRole())){
            throw  new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
        }
        if(userEntity == null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }
        String[] encryptedText = PasswordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
          return  userDao.DeleteUser(userEntity);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity Signout( final String authorizationToken) throws UserNotFoundException, UnauthorizedException, SignOutRestrictedException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);
        UserEntity userEntity = userAuthTokenEntity.getUser();
 if (userAuthTokenEntity== null){
     throw new SignOutRestrictedException("SGR-001","User is not Signed in");

 }
 else {
     userAuthTokenEntity.setLogoutAt(ZonedDateTime.now());

     userDao.updateAuth(userAuthTokenEntity);

     return userEntity;
 }

    }
    }


