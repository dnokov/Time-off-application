package com.learning.timeOffManagement.exception;

public class TimeOffAlreadySubmittedException extends RuntimeException {
    private static final String TIME_OFF_NOT_ALREADY_SUBMITTED = "Time off with id %s is submitted and cannot be reviewed";

    public TimeOffAlreadySubmittedException(Long id) {
        super(String.format(TIME_OFF_NOT_ALREADY_SUBMITTED, id));
    }
}
