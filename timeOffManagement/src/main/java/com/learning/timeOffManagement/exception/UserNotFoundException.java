package com.learning.timeOffManagement.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String USER_NOT_FOUND_TEMPLATE = "User with %s %s is not found.";
    private static final String USERNAME = "username:";
    private static final String ID = "id:";

    public UserNotFoundException(Long id) {
        super(String.format(USER_NOT_FOUND_TEMPLATE, ID, id));
    }

    public UserNotFoundException(String username) {
        super(String.format(USER_NOT_FOUND_TEMPLATE, USERNAME, username));
    }
}
