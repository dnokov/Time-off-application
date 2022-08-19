package com.learning.timeOffManagement.utils;

public class TokenServiceProperties {
    public static final String BEARER_ID_FIELD = "id";
    public static final String BEARER_SUBJECT_FIELD = "sub";
    public static final String BEARER_ADMIN_FIELD = "ADMIN";
    public static final String SECRET_PROPERTY = "${jwt.secret}";

    private TokenServiceProperties(){
    }
}
