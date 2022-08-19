package com.learning.timeOffManagement.exception;

public class TimeOffDateException extends RuntimeException {

    private static final String TIME_OFF_DATE_TEMPLATE = "start date %s cannot be after end date %s .";

    private static final String TIME_OFF_START_DATE_TEMPLATE = "start date %s cannot be before current date.";

    public TimeOffDateException(String startDate, String endDate) {
        super(String.format(TIME_OFF_DATE_TEMPLATE, startDate, endDate));
    }

    public TimeOffDateException(String startDate) {
        super(String.format(TIME_OFF_START_DATE_TEMPLATE, startDate));
    }
}
