package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.model.User;
import com.learning.timeOffManagement.security.UserPrincipal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
public class TokenServiceImplTests {

    @Autowired
    public TokenService tokenService;

    public static User dummyUser;
    public String token;

    @Test
    public void createTokenForUserShouldHaveSameId(){
        UserPrincipal userPrincipal = tokenService.parseToken(token);
        Long expectedId = dummyUser.getId();
        Long actualId = userPrincipal.getId();
        assertEquals(expectedId, actualId);
    }

    @Test
    public void createTokenForUserShouldHaveSameUsername(){
        UserPrincipal userPrincipal = tokenService.parseToken(token);
        String expectedUsername = dummyUser.getUsername();
        String actualUsername = userPrincipal.getUserName();
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void createTokenForUserShouldHaveSameAdminStatus(){
        UserPrincipal userPrincipal = tokenService.parseToken(token);
        boolean expectedStatus = dummyUser.getIsAdmin();
        boolean actualStatus = userPrincipal.isAdmin();
        assertEquals(expectedStatus, actualStatus);
    }

    @BeforeAll
    public static void setUpUser(){
        Long id = 2L;
        String username = "admin";
        String password = "adminpass";
        String firstName = "admin";
        String lastName = "adminsson";
        Long creatorId = 2L;
        Boolean isAdmin = true;
        String email = "adminmainl@gmail.com";
        dummyUser = new User(username, password, firstName, lastName, creatorId, isAdmin, email);
        dummyUser.setId(id);
    }

    /**
     * Creates a token and strips it from bearer prefix
     */
    @BeforeEach
    public void setUpToken(){
        token = tokenService.generateToken(dummyUser).replace("Bearer ","");
    }

}
