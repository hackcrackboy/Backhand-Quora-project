package com.upgrad.quora.api.Controller;
import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.Entity.UserAuthTokenEntity;
import com.upgrad.quora.service.Entity.UserEntity;
import com.upgrad.quora.service.business.PasswordCryptographyProvider;
import com.upgrad.quora.service.business.UserAuthbuisness;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.dao.Dao;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.UUID;



@EnableAutoConfiguration
@RequestMapping("/users/")
@RestController
@Component
public class UserController {
    @Autowired
    private UserBusinessService userBusinessService;
    @Autowired
    private Dao userDao;
    @Autowired
    private UserAuthbuisness userAuthbuisness;

    @RequestMapping(method = RequestMethod.POST, path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse>Signup(@RequestBody final SignupUserRequest userRequest) throws SignUpRestrictedException {

        UserEntity userEntity = new UserEntity();
        userEntity.setAboutMe(userRequest.getAboutMe());
        userEntity.setContactNumber(userRequest.getContactNumber());
        userEntity.setCountry(userRequest.getCountry());
        userEntity.setDob(userRequest.getDob());

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userEntity.setUserName(userRequest.getUserName());
        userEntity.setEmail(userRequest.getEmailAddress());
        String[] encryptedText = PasswordCryptographyProvider.encrypt(userRequest.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        userEntity.setRole("nonadmin");


        UserEntity createdUser1 = userBusinessService.Signup(userEntity);
        final UserEntity createdUser = userDao.Signup(createdUser1);

        final SignupUserResponse userResponse = new SignupUserResponse().id(createdUser.getUuid()).status("USER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);

    }
    @RequestMapping(method = RequestMethod.POST, path = "/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AuthorizedUserResponse>Signin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException, SignUpRestrictedException {
        //Basic dXNlcm5hbWU6cGFzc3dvcmQ=
        //above is a sample encoded text where the username is "username" and password is "password" separated by a ":"
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        UserAuthTokenEntity userAuthToken = userAuthbuisness.Authenticate(decodedArray[0], decodedArray[1]);
        UserEntity user = userAuthToken.getUser();
        userAuthToken.setUuid(user.getUuid());
        final UserAuthTokenEntity user12 = userDao.createAuthToken(userAuthToken);

        AuthorizedUserResponse authorizedUserResponse = new AuthorizedUserResponse().id(UUID.fromString(user12.getUuid()))
                .firstName(user.getFirstName()).lastName(user.getLastName())
                .emailAddress(user.getEmail()).mobilePhone(user.getContactNumber())
                .lastLoginTime(user12.getLoginAt())
                .status("SIGNED IN SUCCESSFULLY");


        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthToken.getAccessToken());
        return new ResponseEntity<AuthorizedUserResponse>(authorizedUserResponse, headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> Signout(@RequestHeader("authorization") final String authorization) throws UserNotFoundException, UnauthorizedException, SignOutRestrictedException {
        final UserEntity userEntity = userBusinessService.Signout(authorization);
        SignoutResponse userDetailsResponse = new SignoutResponse().id(userEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(userDetailsResponse, HttpStatus.OK);


    }
}

