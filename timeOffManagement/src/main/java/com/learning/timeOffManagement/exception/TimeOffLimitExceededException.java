package com.learning.timeOffManagement.exception;

public class TimeOffLimitExceededException extends RuntimeException {

    private static final String TIME_OFF_LIMIT_EXCEEDED_TEMPLATE = "Time off limit exceeded %s %s.";

    public TimeOffLimitExceededException(String startDate, String endDate) {
        super(String.format(TIME_OFF_LIMIT_EXCEEDED_TEMPLATE, startDate, endDate));
    }
}
