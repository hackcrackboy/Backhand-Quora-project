package com.upgrad.quora.service.business;

import com.upgrad.quora.service.Entity.UserAuthTokenEntity;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.dao.Dao;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
@Service
public class UserAuthbuisness {
@Autowired
    private Dao userDao;
@Transactional(propagation = Propagation.REQUIRED)
public UserAuthTokenEntity Authenticate(final String username, final String password) throws AuthenticationFailedException {
    UserEntity userEntity = userDao.getUserByUsername(username);
    if (userEntity == null) {
        throw new AuthenticationFailedException("ATH-001", "This username does not exist");
    }
    if(userEntity.getPassword().equals(password)){}
    final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, userEntity.getSalt());

    if(encryptedPassword.equals(userEntity.getPassword())){
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
        UserAuthTokenEntity userAuthToken = new UserAuthTokenEntity();
        userAuthToken.setUser(userEntity);

        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));

        userAuthToken.setLoginAt(now);
        userAuthToken.setExpiresAt(expiresAt);
        userDao.createAuthToken(userAuthToken);
        userDao.updateUser(userEntity);


        return userAuthToken;
    }
    else{
        throw new AuthenticationFailedException("ATH-002", "Password Failed");
    }


}}