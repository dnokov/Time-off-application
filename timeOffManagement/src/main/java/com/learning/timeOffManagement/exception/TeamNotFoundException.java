package com.learning.timeOffManagement.exception;

public class TeamNotFoundException extends RuntimeException {

    private static final String TEAM_NOT_FOUND_TEMPLATE = "Team with id: %s not found";

    public TeamNotFoundException(Long id) {
        super(String.format(TEAM_NOT_FOUND_TEMPLATE, id));
    }

    public TeamNotFoundException(String title) {
        super(String.format(TEAM_NOT_FOUND_TEMPLATE, title));
    }
}
